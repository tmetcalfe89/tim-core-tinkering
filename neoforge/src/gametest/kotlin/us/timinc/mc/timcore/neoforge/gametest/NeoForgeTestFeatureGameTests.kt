package us.timinc.mc.timcore.neoforge.gametest

import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.neoforged.neoforge.gametest.GameTestHolder
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate
import us.timinc.mc.timcore.gametest.TestFeatureGameTests

@GameTestHolder("tim_core_gametest")
@PrefixGameTestTemplate(false)
object NeoForgeTestFeatureGameTests {
    @JvmStatic
    @GameTest(template = "empty")
    fun registersTestItems(helper: GameTestHelper) = TestFeatureGameTests.registersTestItems(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun registersTestBlocks(helper: GameTestHelper) = TestFeatureGameTests.registersTestBlocks(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun registersTestBlockEntityType(helper: GameTestHelper) =
        TestFeatureGameTests.registersTestBlockEntityType(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun createsTestBlockEntity(helper: GameTestHelper) = TestFeatureGameTests.createsTestBlockEntity(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun registersTagItemDropEntry(helper: GameTestHelper) = TestFeatureGameTests.registersTagItemDropEntry(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun multipliesEvGainThroughCobblemonEvent(helper: GameTestHelper) =
        TestFeatureGameTests.multipliesEvGainThroughCobblemonEvent(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun awardsExpAllExperience(helper: GameTestHelper) = TestFeatureGameTests.awardsExpAllExperience(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun requiresPartyToFishPokemon(helper: GameTestHelper) =
        TestFeatureGameTests.requiresPartyToFishPokemon(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun persistsQuickBallImmunityProperty(helper: GameTestHelper) =
        TestFeatureGameTests.persistsQuickBallImmunityProperty(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun ignoresCatchRateEventsWithoutQuickBallBonus(helper: GameTestHelper) =
        TestFeatureGameTests.ignoresCatchRateEventsWithoutQuickBallBonus(helper)

    @JvmStatic
    @GameTest(template = "empty")
    fun preventsRepeatedTurnOneQuickBallBonus(helper: GameTestHelper) =
        TestFeatureGameTests.preventsRepeatedTurnOneQuickBallBonus(helper)
}
