import dayjs from "dayjs";
import { Config } from "$lib/config";

export interface TransferResult {
  id: string;
  key: string;
  expireAt: string;
  createdAt: string;
}

export interface DurationOption {
  amount: number;
  unit: "hour" | "day" | "week" | "month";
}

export class TransferService {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  async createTransfer(
    files: File[],
    duration: DurationOption,
    password?: string,
  ): Promise<TransferResult> {
    const formData = new FormData();
    files.forEach((file) => {
      formData.append("files", file);
    });
    if (password) {
      formData.append("password", password);
    }

    if (duration) {
      const expireAt = dayjs()
        .add(duration.amount, duration.unit)
        .toISOString();
      formData.append("expire_at", expireAt);
    }

    const response = await fetch(`${this.baseUrl}/transfers`, {
      method: "POST",
      body: formData,
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to create transfer");
    }

    return await response.json();
  }

  async retrieveTransfer(
    key: string,
    password?: string | null,
  ): Promise<{ blob: Blob; filename: string }> {
    const response = await fetch(`${this.baseUrl}/transfers/${key}`, {
      method: "POST",
      headers: {
        Accept: "*/*",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        password: password,
      }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to retrieve transfer");
    }

    const filename =
      response.headers
        .get("content-disposition")
        ?.split(";")
        .find((n) => n.includes("filename="))
        ?.replace("filename=", "")
        ?.replaceAll('"', "")
        .trim() ?? "files.zip";

    const blob = await response.blob();
    return { blob, filename };
  }
}

export const transferService = new TransferService(Config.apiBaseUrl);
