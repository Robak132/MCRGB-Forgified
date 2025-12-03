package com.bacco.libgui;

import com.bacco.mixin.ScreenAccessor;
import com.mojang.blaze3d.platform.Lighting;
import com.bacco.libgui.widget.WPanel;
import com.bacco.libgui.widget.WWidget;
import com.bacco.libgui.widget.data.InputResult;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/**
 * A screen for a {@link SyncedGuiDescription}.
 *
 * @param <T> the description type
 */
public class CottonInventoryScreen<T extends SyncedGuiDescription> extends AbstractContainerScreen<T> implements CottonScreenImpl {
    private static final VisualLogger LOGGER = new VisualLogger(CottonInventoryScreen.class);
    private final MouseInputHandler<CottonInventoryScreen<T>> mouseInputHandler = new MouseInputHandler<>(this);
    protected SyncedGuiDescription description;
    @Nullable
    protected WWidget lastResponder = null;

    /**
     * Constructs a new screen without a title.
     *
     * @param description the GUI description
     * @param inventory   the player inventory
     * @since 5.2.0
     */
    public CottonInventoryScreen(T description, Inventory inventory) {
        this(description, inventory, CommonComponents.EMPTY);
    }

    /**
     * Constructs a new screen.
     *
     * @param description the GUI description
     * @param inventory   the player inventory
     * @param title       the screen title
     * @since 5.2.0
     */
    public CottonInventoryScreen(T description, Inventory inventory, Component title) {
        super(description, inventory, title);
        this.description = description;
        width = 18 * 9;
        height = 18 * 9;
        this.imageWidth = 18 * 9;
        this.imageHeight = 18 * 9;
        description.getRootPanel().validate(description);
    }

    /**
     * Constructs a new screen without a title.
     *
     * @param description the GUI description
     * @param player      the player
     */
    public CottonInventoryScreen(T description, Player player) {
        this(description, player.getInventory());
    }

    /**
     * Constructs a new screen.
     *
     * @param description the GUI description
     * @param player      the player
     * @param title       the screen title
     */
    public CottonInventoryScreen(T description, Player player, Component title) {
        this(description, player.getInventory(), title);
    }

    /*
     * RENDERING NOTES:
     *
     * * "width" and "height" are the width and height of the overall screen
     * * "backgroundWidth" and "backgroundHeight" are the width and height of the panel to render
     * * ~~"left" and "top" are *actually* self-explanatory~~
     *   * "left" and "top" are now (1.15) "x" and "y". A bit less self-explanatory, I guess.
     * * coordinates start at 0,0 at the topleft of the screen.
     */

    @Override
    public void init() {
        super.init();

        WPanel root = description.getRootPanel();
        if (root != null) root.addPainters();
        description.addPainters();

        reposition(width, height);

        if (root != null) {
            GuiEventListener rootPanelElement = FocusElements.ofPanel(root);
            ((ScreenAccessor) this).mcrgb$getChildren().add(rootPanelElement);
            setInitialFocus(rootPanelElement);
        } else {
            LOGGER.warn("No root panel found, keyboard navigation disabled");
        }
    }

    @Override
    public void removed() {
        super.removed();
        VisualLogger.reset();
    }

    @ApiStatus.Internal
    @Override
    public GuiDescription getDescription() {
        return description;
    }

    @Nullable
    @Override
    public WWidget getLastResponder() {
        return lastResponder;
    }

    @Override
    public void setLastResponder(@Nullable WWidget lastResponder) {
        this.lastResponder = lastResponder;
    }

    /**
     * Clears the heavyweight peers of this screen's GUI description.
     */
    private void clearPeers() {
        description.slots.clear();
    }

    /**
     * Repositions the root panel.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    protected void reposition(int screenWidth, int screenHeight) {
        WPanel basePanel = description.getRootPanel();
        if (basePanel != null) {
            clearPeers();
            basePanel.validate(description);

            imageWidth = basePanel.getWidth();
            imageHeight = basePanel.getHeight();

            //DEBUG
            if (imageWidth < 16) imageWidth = 300;
            if (imageHeight < 16) imageHeight = 300;
        }

        titleLabelX = description.getTitlePos().x();
        titleLabelY = description.getTitlePos().y();

        if (!description.isFullscreen()) {
            inventoryLabelX = (screenWidth / 2) - (imageWidth / 2);
            inventoryLabelY = (screenHeight / 2) - (imageHeight / 2);
        } else {
            inventoryLabelX = 0;
            inventoryLabelY = 0;

            if (basePanel != null) {
                basePanel.setSize(screenWidth, screenHeight);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        //...yeah, we're going to go ahead and override that.
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int containerX = (int) mouseX - inventoryLabelX;
        int containerY = (int) mouseY - inventoryLabelY;
        mouseInputHandler.checkFocus(containerX, containerY);
        if (containerX < 0 || containerY < 0 || containerX >= width || containerY >= height) return true;
        mouseInputHandler.onMouseDown(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        int containerX = (int) mouseX - inventoryLabelX;
        int containerY = (int) mouseY - inventoryLabelY;
        mouseInputHandler.onMouseUp(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);

        int containerX = (int) mouseX - inventoryLabelX;
        int containerY = (int) mouseY - inventoryLabelY;
        mouseInputHandler.onMouseDrag(containerX, containerY, mouseButton, deltaX, deltaY);

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        super.mouseScrolled(mouseX, mouseY, amount);

        int containerX = (int) mouseX - inventoryLabelX;
        int containerY = (int) mouseY - inventoryLabelY;
        mouseInputHandler.onMouseScroll(containerX, containerY, amount);

        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        int containerX = (int) mouseX - inventoryLabelX;
        int containerY = (int) mouseY - inventoryLabelY;
        mouseInputHandler.onMouseMove(containerX, containerY);
    }

    @Override
    public boolean charTyped(char ch, int keyCode) {
        WWidget focus = description.getFocus();
        if (focus != null && focus.onCharTyped(ch) == InputResult.PROCESSED) {
            return true;
        }

        return super.charTyped(ch, keyCode);
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        WWidget focus = description.getFocus();
        if (focus != null && focus.onKeyPressed(ch, keyCode, modifiers) == InputResult.PROCESSED) {
            return true;
        }

        return super.keyPressed(ch, keyCode, modifiers);
    }

    @Override
    public boolean keyReleased(int ch, int keyCode, int modifiers) {
        WWidget focus = description.getFocus();
        if (focus != null && focus.onKeyReleased(ch, keyCode, modifiers) == InputResult.PROCESSED) {
            return true;
        }

        return super.keyReleased(ch, keyCode, modifiers);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics context, float partialTicks, int mouseX, int mouseY) {
        // This is just an AbstractContainerScreen thing; most Screens don't work this way.
    }

    private void paint(GuiGraphics context, int mouseX, int mouseY) {
        renderBackground(context);

        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                Scissors.refreshScissors();
                root.paint(context, inventoryLabelX, inventoryLabelY, mouseX - inventoryLabelX, mouseY - inventoryLabelY);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                Scissors.checkStackIsEmpty();
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        paint(context, mouseX, mouseY);

        super.render(context, mouseX, mouseY, partialTicks);
        Lighting.setupForFlatItems(); //Needed because super.render leaves dirty state

        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                WWidget hitChild = root.hit(mouseX - inventoryLabelX, mouseY - inventoryLabelY);
                if (hitChild != null)
                    hitChild.renderTooltip(context, inventoryLabelX, inventoryLabelY, mouseX - inventoryLabelX, mouseY - inventoryLabelY);
            }
        }

        renderTooltip(context, mouseX, mouseY);
        VisualLogger.render(context);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics context, int mouseX, int mouseY) {
        if (description != null && description.isTitleVisible()) {
            int width = description.getRootPanel().getWidth();
            ScreenDrawing.drawString(context, getTitle().getVisualOrderText(), description.getTitleAlignment(), titleLabelX, titleLabelY, width - 2 * titleLabelX, description.getTitleColor());
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                root.tick();
            }
        }
    }

    @Override
    protected void updateNarratedWidget(@NotNull NarrationElementOutput builder) {
        if (description != null) NarrationHelper.addNarrations(description.getRootPanel(), builder);
    }
}
