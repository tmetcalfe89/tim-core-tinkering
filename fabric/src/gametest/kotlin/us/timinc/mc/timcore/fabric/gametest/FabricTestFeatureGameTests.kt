package us.timinc.mc.timcore.fabric.gametest

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import us.timinc.mc.timcore.gametest.TestFeatureGameTests

class FabricTestFeatureGameTests : FabricGameTest {
    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun registersTestItems(helper: GameTestHelper) = TestFeatureGameTests.registersTestItems(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun registersTestBlocks(helper: GameTestHelper) = TestFeatureGameTests.registersTestBlocks(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun registersTestBlockEntityType(helper: GameTestHelper) =
        TestFeatureGameTests.registersTestBlockEntityType(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun createsTestBlockEntity(helper: GameTestHelper) = TestFeatureGameTests.createsTestBlockEntity(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun registersTagItemDropEntry(helper: GameTestHelper) = TestFeatureGameTests.registersTagItemDropEntry(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun multipliesEvGainThroughCobblemonEvent(helper: GameTestHelper) =
        TestFeatureGameTests.multipliesEvGainThroughCobblemonEvent(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun persistsQuickBallImmunityProperty(helper: GameTestHelper) =
        TestFeatureGameTests.persistsQuickBallImmunityProperty(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun ignoresCatchRateEventsWithoutQuickBallBonus(helper: GameTestHelper) =
        TestFeatureGameTests.ignoresCatchRateEventsWithoutQuickBallBonus(helper)

    @GameTest(template = TestFeatureGameTests.EMPTY_TEMPLATE)
    fun preventsRepeatedTurnOneQuickBallBonus(helper: GameTestHelper) =
        TestFeatureGameTests.preventsRepeatedTurnOneQuickBallBonus(helper)
}
