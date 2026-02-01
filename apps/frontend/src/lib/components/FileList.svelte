<script lang="ts">
	let { files, onRemove }: { files: File[]; onRemove: (index: number) => void } = $props();
</script>

<ul class="flex flex-col gap-4">
	{#each files as file, i}
		<li class="flex justify-between items-center">
			<span>{file.name} ({Math.round(file.size / 1024)} KB)</span>
			<button class="btn btn-warning" onclick={() => onRemove(i)}>Remove</button>
		</li>
	{/each}
</ul>
