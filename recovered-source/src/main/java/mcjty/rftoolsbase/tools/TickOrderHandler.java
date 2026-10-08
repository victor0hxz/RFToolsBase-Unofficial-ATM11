package mcjty.rftoolsbase.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class TickOrderHandler {
   private static final Map<ResourceKey<Level>, List<TickOrderHandler.IOrderTicker>[]> tiles = new HashMap<>();
   private static long ticker = 0L;

   public static void clean() {
      tiles.clear();
   }

   public static long getTicker() {
      return ticker;
   }

   public static void queue(TickOrderHandler.IOrderTicker tile) {
      if (!tiles.containsKey(tile.getDimension())) {
         List<TickOrderHandler.IOrderTicker>[] list = new List[TickOrderHandler.Rank.values().length];

         for (TickOrderHandler.Rank rank : TickOrderHandler.Rank.values()) {
            list[rank.ordinal()] = new ArrayList<>();
         }

         tiles.put(tile.getDimension(), list);
      }

      tiles.get(tile.getDimension())[tile.getRank().ordinal()].add(tile);
   }

   private static <T extends TickOrderHandler.IOrderTicker> void tickServer(List<T> tileEntities) {
      for (T tileEntity : tileEntities) {
         tileEntity.tickOnServer();
      }

      tileEntities.clear();
   }

   public static void postWorldTick(ResourceKey<Level> dimension) {
      ticker++;
      List<TickOrderHandler.IOrderTicker>[] lists = tiles.get(dimension);
      if (lists != null) {
         for (TickOrderHandler.Rank rank : TickOrderHandler.Rank.values()) {
            tickServer(lists[rank.ordinal()]);
         }
      }
   }

   public interface IOrderTicker {
      TickOrderHandler.Rank getRank();

      void tickOnServer();

      ResourceKey<Level> getDimension();
   }

   public static enum Rank {
      RANK_0,
      RANK_1,
      RANK_2,
      RANK_3,
      RANK_4;
   }
}
