package com.github.onran0.jsw.block.meta;

import com.github.onran0.jsw.weapon.ProjectileType;

import java.util.HashMap;
import java.util.Map;

public final class BlockInfo {
    private final String name;
    private final BlockCategory category;
    private final float weight;
    private final float strength;
    private final Map<ScoreType, Float> scores;
    private final Map<ProjectileType, Float> damageResistances;
    private final float scoresMultiplier;
    private final int scoresMultiplyPer;

    public BlockInfo(
            final String name,
            final BlockCategory category, final float weight,
            final float strength, final float scoresMultiplier,
            final int scoresMultiplyPer, final float[] scores,
            final float[] damageResistances
    ) {
        this.name = name;

        this.category = category;
        this.weight = weight;
        this.strength = strength;
        this.scoresMultiplier = scoresMultiplier;
        this.scoresMultiplyPer = scoresMultiplyPer;

        this.scores = new HashMap<>();

        for(int i = 0;i < scores.length;i++)
            this.scores.put(ScoreType.values()[i], scores[i]);

        this.damageResistances = new HashMap<>();

        for(int i = 0;i < damageResistances.length;i++)
            this.damageResistances.put(ProjectileType.values()[i], damageResistances[i]);
    }

    public String getName() {
        return name;
    }

    public BlockCategory getCategory() {
        return category;
    }

    public float getWeight() {
        return weight;
    }

    public float getStrength() {
        return strength;
    }

    public float getScores(final ScoreType type) {
        return scores.get(type);
    }

    public float getResistance(final ProjectileType type) {
        return damageResistances.get(type);
    }

    public float getScoresMultiplier() {
        return scoresMultiplier;
    }

    public int getScoresMultiplyPer() {
        return scoresMultiplyPer;
    }

    public float getScores(final ScoreType type, final int count) {
        final float points = this.getScores(type);

        float total = 0f;

        for(int i = 0;i < count;i++)
            total += (float) (points * (1 + Math.floor(i / (float) scoresMultiplyPer) * (this.scoresMultiplier - 1f)));

        return total;
    }
}