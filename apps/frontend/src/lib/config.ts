import { env } from '$env/dynamic/public';

export const Config = {
    get apiBaseUrl() {
        return env.PUBLIC_API_BASE_URL || 'http://localhost:8080';
    }
};
