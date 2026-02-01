<script lang="ts">
	let { onRetrieve }: { onRetrieve: (key: string, password: string | null) => void } = $props();

	let retrieveKey = $state('');
	let retrievePassword: string | null = $state(null);

	function handleRetrieve() {
		onRetrieve(retrieveKey, retrievePassword);
	}
</script>

<div class="flex flex-col gap-4 w-1/2">
	<span>Recuperer</span>
	<input type="text" class="input w-full" bind:value={retrieveKey} placeholder="Id" />
	<input type="password" class="input w-full" bind:value={retrievePassword} placeholder="Mot de passe" />
	<button class="btn btn-soft" onclick={handleRetrieve}>Telecharger</button>
</div>
