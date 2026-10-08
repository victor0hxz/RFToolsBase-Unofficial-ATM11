package mcjty.rftoolsbase.modules.crafting.data;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record CraftingCardData(List<ItemStack> stacks) {
   public static final Codec<CraftingCardData> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(CraftingCardData::new, CraftingCardData::stacks);
   public static final StreamCodec<RegistryFriendlyByteBuf, CraftingCardData> STREAM_CODEC = StreamCodec.composite(
      ItemStack.OPTIONAL_LIST_STREAM_CODEC, CraftingCardData::stacks, CraftingCardData::new
   );

   public CraftingCardData withStacks(List<ItemStack> stacks) {
      return new CraftingCardData(stacks);
   }
}
