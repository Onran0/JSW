package com.github.onran0.jsw.weapon;

import java.util.*;

public final class WeaponInfo {

    private final float projectileSwitchingDuration;
    private final float fullReloadDuration;
    private final Map<ProjectileType, Float> damages;
    private final Map<ProjectileType, Float> maxDamages;
    private final Map<ProjectileType, Float> velocities;
    private final List<ProjectileType> supportedProjectileTypes;

    public WeaponInfo(
            final float projectileSwitchingDuration,
            final float fullReloadDuration,
            final ProjectileType[] supportedProjectileTypes,
            final float[] damages,
            final float[] maxDamages,
            final float[] velocities
    ) {
        this.projectileSwitchingDuration = projectileSwitchingDuration;
        this.fullReloadDuration = fullReloadDuration;

        this.supportedProjectileTypes = new ArrayList<>();

        Collections.addAll(this.supportedProjectileTypes, supportedProjectileTypes);

        this.damages = new HashMap<>();
        this.maxDamages = new HashMap<>();
        this.velocities = new HashMap<>();

        for(int i = 0;i < supportedProjectileTypes.length;i++) {
            final ProjectileType type = supportedProjectileTypes[i];

            this.damages.put(type, damages[i]);
            this.maxDamages.put(type, maxDamages[i]);
            this.velocities.put(type, velocities[i]);
        }
    }

    public float getProjectileSwitchingDuration() {
        return projectileSwitchingDuration;
    }

    public float getFullReloadDuration() {
        return fullReloadDuration;
    }

    public List<ProjectileType> getSupportedProjectileTypes() {
        return new ArrayList<>(this.supportedProjectileTypes);
    }

    public boolean isSupportedProjectileType(final ProjectileType type) {
        return this.supportedProjectileTypes.contains(type);
    }

    public float getDamage(final ProjectileType type) {
        return damages.get(type);
    }

    public float getMaxDamage(final ProjectileType type) {
        return maxDamages.get(type);
    }

    public float getVelocity(final ProjectileType type) {
        return velocities.get(type);
    }

    public int getFragmentsCount(final ProjectileType type) {
        return (int) Math.ceil(getMaxDamage(type) / getDamage(type));
    }
}