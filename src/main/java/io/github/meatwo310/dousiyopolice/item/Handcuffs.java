package io.github.meatwo310.dousiyopolice.item;

import io.github.meatwo310.dousiyopolice.config.ServerConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Handcuffs extends Item {
    public static final String NBT_POS = "SavedPos";

    public Handcuffs() {
        super(new Item.Properties().stacksTo(1));
    }

    public Handcuffs(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null || !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (ctx.getLevel().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clickedPos = ctx.getClickedPos();
        ctx.getItemInHand().addTagElement(NBT_POS, LongTag.valueOf(clickedPos.asLong()));
        player.sendSystemMessage(Component.literal("Location saved: " + clickedPos.toShortString()));

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (player.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!ServerConfig.getAllowed().check(player)) {
            player.sendSystemMessage(Component.literal("You don't have permission to use the handcuffs!"));
            return InteractionResult.FAIL;
        }

        boolean isPlayer = entity instanceof Player;

        if (ServerConfig.HANDCUFFS_PLAYER_ONLY.get() && !isPlayer) {
            player.sendSystemMessage(Component.literal("You can only use the handcuffs on players!"));
            return InteractionResult.FAIL;
        }

        var tag = stack.getTag();
        if (tag == null || !tag.contains(NBT_POS)) {
            player.sendSystemMessage(Component.literal(
                    "No location saved! Sneak-right-click a block to save one."
            ));
            return InteractionResult.FAIL;
        }

        BlockPos pos = BlockPos.of(tag.getLong(NBT_POS));
        player.sendSystemMessage(Component.empty()
                .append("You handcuffed ")
                .append(entity.getDisplayName())
                .append("!")
        );
        entity.teleportTo(
                pos.getX() + 0.5,
                pos.getY() + 1,
                pos.getZ() + 0.5
        );
        if (isPlayer) {
            entity.sendSystemMessage(Component.empty()
                    .append("You have been handcuffed by ")
                    .append(player.getDisplayName())
                    .append("!")
            );
        }

        return InteractionResult.SUCCESS;
    }
}
