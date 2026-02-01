<script lang="ts">
	import { transferService, type TransferResult, type DurationOption } from '$lib/services/transfer.service';
	import RetrieveForm from '$lib/components/RetrieveForm.svelte';
	import UploadDropzone from '$lib/components/UploadDropzone.svelte';
	import FileList from '$lib/components/FileList.svelte';
	import TransferOptions from '$lib/components/TransferOptions.svelte';
	import TransferResultCmp from '$lib/components/TransferResult.svelte';

	let files: File[] = $state([]);
	let password = $state('');
	
	let selectedDuration = $state(null);

	let uploading = $state(false);
	let result: TransferResult | null = $state(null);
	let error: string | null = $state(null);

	function handleFilesSelected(newFiles: File[]) {
		files = [...files, ...newFiles];
	}

	async function createTransfer() {
		if (files.length === 0) return;

		uploading = true;
		error = null;
		result = null;

		try {
			result = await transferService.createTransfer(files, selectedDuration ?? { amount: 1, unit: 'day' }, password);
		} catch (e: any) {
			console.error(e);
			error = e.message;
		} finally {
			uploading = false;
		}
	}

	function downloadData(blob: Blob, name: string) {
		const a = document.createElement('a');
		document.body.append(a);
		a.download = name;
		a.href = URL.createObjectURL(blob);
		a.click();
		a.remove();
	}

	async function retrieveTransfer(key: string, password: string | null) {
		try {
			const { blob, filename } = await transferService.retrieveTransfer(key, password);
			downloadData(blob, filename);
		} catch (e: any) {
			console.error(e);
			error = e.message;
		}
	}

	function removeFile(index: number) {
		files = files.filter((_, i) => i !== index);
	}
</script>

<main class="w-full h-screen flex justify-center p-10">
	<div class="card w-full bg-base-200 card-lg shadow-sm h-fit">
		<div class="card-body">
			<h1 class="w-full text-center text-2xl font-medium pb-5">Taki Transfer</h1>

			<div class="flex flex-row">
				<RetrieveForm onRetrieve={retrieveTransfer} />

				<div class="divider divider-horizontal"></div>

				<UploadDropzone onFilesSelected={handleFilesSelected} />
			</div>

			{#if files.length > 0}
				<div class="divider divider-vertical"></div>
				<div class="flex flex-col gap-4">
					<h3>Selected Files : </h3>
					<FileList {files} onRemove={removeFile} />

					<TransferOptions bind:password bind:selectedDuration />

					<button class="btn btn-accent" onclick={createTransfer} disabled={uploading}>
						{uploading ? 'Uploading...' : 'Create Transfer'}
					</button>
				</div>
			{/if}

			{#if error}
				<div class="border-error border-2 rounded-lg p-4">
					<p class="text-error">Erreur : {error}</p>
				</div>
			{/if}

			{#if result}
				<TransferResultCmp {result} />
			{/if}
		</div>
	</div>
</main>
