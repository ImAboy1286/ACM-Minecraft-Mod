package com.acm.mixin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
    @Shadow
    @Final
    @Mutable
    private static List<List<RegistryEntry<StatusEffect>>> EFFECTS_BY_LEVEL;

    @Shadow
    @Final
    @Mutable
    private static Set<RegistryEntry<StatusEffect>> EFFECTS;

    @ModifyConstant(
        method = "updateLevel",
        constant = @Constant(intValue = 4)
    )
    private static int acm$allowAdditionalBeaconLayers(int vanillaMaximumLevel) {
        // Continue scanning until the world height stops the beacon naturally.
        return Integer.MAX_VALUE;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void acm$addSaturationBeaconEffect(CallbackInfo callbackInfo) {
        List<List<RegistryEntry<StatusEffect>>> effectsByLevel = new ArrayList<>(EFFECTS_BY_LEVEL);
        List<RegistryEntry<StatusEffect>> tierThreeEffects = new ArrayList<>(effectsByLevel.get(2));
        tierThreeEffects.add(StatusEffects.SATURATION);
        effectsByLevel.set(2, List.copyOf(tierThreeEffects));
        EFFECTS_BY_LEVEL = List.copyOf(effectsByLevel);

        Set<RegistryEntry<StatusEffect>> effects = new HashSet<>(EFFECTS);
        effects.add(StatusEffects.SATURATION);
        EFFECTS = Set.copyOf(effects);
    }

    @ModifyArgs(
        method = "applyPlayerEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/effect/StatusEffectInstance;<init>(Lnet/minecraft/registry/entry/RegistryEntry;IIZZ)V",
            ordinal = 1
        )
    )
    private static void acm$makeSaturationSecondaryLevelTwo(Args args) {
        if (args.get(0).equals(StatusEffects.SATURATION)) {
            args.set(2, 1);
        }
    }
}
