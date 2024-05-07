package net.nerdorg.chess.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.nerdorg.chess.ChessForMinecraft;
import net.nerdorg.chess.block.ModBlocks;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        LOGGER.info("Generating recipes for Minehop");

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHESS_BLOCK)
                .pattern("   ")
                .pattern("WWW")
                .pattern(" F ")
                .input('W', ItemTags.PLANKS)
                .input('F', ItemTags.FENCES)
                .criterion("has_planks", conditionsFromTag(ItemTags.PLANKS))
                .criterion("has_fence", conditionsFromTag(ItemTags.FENCES))
                .offerTo(exporter, new Identifier(ChessForMinecraft.MOD_ID, "chess_board"));
    }
}
