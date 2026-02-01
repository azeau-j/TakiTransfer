<script lang="ts">
	import type { TransferResult as ITransferResult } from '$lib/services/transfer.service';

	let { result }: { result: ITransferResult } = $props();
</script>

<div class="border-success border-2 rounded-lg p-4">
	<h3>Transfer Created Successfully!</h3>
	<p><strong>Transfer ID:</strong> {result.id}</p>
	{#if result.expireAt}
		<p><strong>Expires At:</strong> {new Date(result.expireAt).toLocaleString()}</p>
	{/if}
</div>
