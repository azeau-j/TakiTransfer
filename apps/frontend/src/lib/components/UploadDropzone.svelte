<script lang="ts">
	let { onFilesSelected }: { onFilesSelected: (files: File[]) => void } = $props();

	function handleFileSelect(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files) {
			onFilesSelected(Array.from(target.files));
		}
	}

	function handleDrop(event: DragEvent) {
		event.preventDefault();
		if (event.dataTransfer?.files) {
			onFilesSelected(Array.from(event.dataTransfer.files));
		}
	}

	function handleDragOver(event: DragEvent) {
		event.preventDefault();
	}
</script>

<div
	class="w-1/2 flex items-center justify-center flex-col border-accent border-2 border-dashed rounded-lg p-6 gap-2"
	ondrop={handleDrop}
	ondragover={handleDragOver}
	role="region"
	aria-label="File drop zone"
>
	<span>Drag and drop files here or</span>
	<input class="file-input" type="file" multiple onchange={handleFileSelect} />
</div>
