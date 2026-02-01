<script lang="ts">
	import type { DurationOption } from '$lib/services/transfer.service';

	let {
		password = $bindable(),
		selectedDuration = $bindable()
	}: { password: string; selectedDuration: DurationOption | null } = $props();

	const durationOptions: { label: string; value: DurationOption }[] = [
		{ label: '1 Hour', value: { amount: 1, unit: 'hour' } },
		{ label: '1 Day', value: { amount: 1, unit: 'day' } },
		{ label: '3 Days', value: { amount: 3, unit: 'day' } },
		{ label: '1 Week', value: { amount: 1, unit: 'week' } },
		{ label: '2 Weeks', value: { amount: 2, unit: 'week' } },
		{ label: '1 Month', value: { amount: 1, unit: 'month' } }
	];
</script>

<fieldset class="fieldset">
	<legend class="fieldset-legend">Expiration :</legend>
	<select class="select w-full" bind:value={selectedDuration}>
        <option value={null}>Selectionner une option (1 jour par défaut)</option>
		{#each durationOptions as option}
			<option value={option.value}>{option.label}</option>
		{/each}
	</select>
</fieldset>

<fieldset class="fieldset">
	<legend class="fieldset-legend">Password :</legend>
	<input type="password" class="input" bind:value={password} placeholder="Protect your files" />
	<p class="label">Optional</p>
</fieldset>
