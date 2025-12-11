package io.github.robak132.mcrgb_forge.gui;

import static io.github.robak132.mcrgb_forge.MCRGB.MOD_ID;

import io.github.robak132.libgui_forge.widget.WButton;
import io.github.robak132.libgui_forge.widget.WGridPanel;
import io.github.robak132.libgui_forge.widget.WLabel;
import io.github.robak132.libgui_forge.widget.WPlainPanel;
import io.github.robak132.libgui_forge.widget.WSlider;
import io.github.robak132.libgui_forge.widget.WTextField;
import io.github.robak132.libgui_forge.widget.WToggleButton;
import io.github.robak132.libgui_forge.widget.data.HorizontalAlignment;
import io.github.robak132.libgui_forge.widget.data.Insets;
import io.github.robak132.libgui_forge.widget.data.Texture;
import io.github.robak132.libgui_forge.widget.icon.TextureIcon;
import io.github.robak132.mcrgb_forge.ColourVector;
import io.github.robak132.mcrgb_forge.IItemBlockColourSaver;
import io.github.robak132.mcrgb_forge.SpriteColour;
import io.github.robak132.mcrgb_forge.SpriteDetails;
import io.github.robak132.mcrgb_forge.client.ClothConfigIntegration;
import io.github.robak132.mcrgb_forge.client.MCRGBClient;
import io.github.robak132.mcrgb_forge.client.MCRGBConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class ColourGui extends MCRGBBaseGui {

    private final List<ItemStack> stacks = new ArrayList<>();
    private final List<WColourGuiSlot> wColourGuiSlots = new ArrayList<>();
    boolean enableSliderListeners = true;
    ColourMode mode = ColourMode.RGB;

    WLabel rLabel = new WLabel(Component.translatable("ui.mcrgb_forge.r_for_red"), 0xFFFF0000);
    WSlider rSlider = new WSlider(0, 255, Direction.Plane.VERTICAL);
    WTextField rInput = new WTextField(Component.literal(Integer.toString(inputColour.r)));

    WLabel gLabel = new WLabel(Component.translatable("ui.mcrgb_forge.g_for_green"), 0xFF00FF00);
    WSlider gSlider = new WSlider(0, 255, Direction.Plane.VERTICAL);
    WTextField gInput = new WTextField(Component.literal(Integer.toString(inputColour.g)));

    WLabel bLabel = new WLabel(Component.translatable("ui.mcrgb_forge.b_for_blue"), 0xFF0000FF);
    WSlider bSlider = new WSlider(0, 255, Direction.Plane.VERTICAL);
    WTextField bInput = new WTextField(Component.literal(Integer.toString(inputColour.b)));

    WButton rgbButton = new WButton(Component.translatable("ui.mcrgb_forge.rgb"));
    WButton hsvButton = new WButton(Component.translatable("ui.mcrgb_forge.hsv"));
    WButton hslButton = new WButton(Component.translatable("ui.mcrgb_forge.hsl"));

    ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
    ItemStack chestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
    ItemStack leggings = new ItemStack(Items.LEATHER_LEGGINGS);
    ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
    ItemStack horse = new ItemStack(Items.LEATHER_HORSE_ARMOR);

    WColourWheel colourWheel = new WColourWheel(ResourceLocation.fromNamespaceAndPath(MOD_ID, "wheel.png"), 0, 0, 1, 1, this);
    WToggleButton colourWheelToggle = new WToggleButton();
    WGradientSlider wheelValueSlider = new WGradientSlider(0, 255, Direction.Plane.VERTICAL);

    WSearchField searchField = new WSearchField(Component.translatable("ui.mcrgb_forge.refine"));
    WPlainPanel sliderArea = new WPlainPanel();
    WColorScrollBar scrollBar = new WColorScrollBar(this::placeSlots);
    WPlainPanel inputs = new WPlainPanel();
    WGridPanel armourSlots = new WGridPanel();

    public ColourGui(ColourVector launchColour) {
        WButtonWithTooltip refreshButton = new WButtonWithTooltip(new TextureIcon(ResourceLocation.fromNamespaceAndPath(MOD_ID, "refresh.png")),
                Component.translatable("ui.mcrgb_forge.refresh_info"));
        WButton settingsButton = new WButton(new TextureIcon(ResourceLocation.fromNamespaceAndPath(MOD_ID, "settings.png")));
        colourSort();
        setRootPanel(root);
        root.add(mainPanel, 0, 0);
        mainPanel.setSize(320, 220);
        mainPanel.setInsets(Insets.ROOT_PANEL);
        mainPanel.add(hexInput, 11, 1, 5, 1);
        mainPanel.add(colourDisplay, 16, 1, 2, 2);
        colourDisplay.setLocation(colourDisplay.getAbsoluteX() + 1, colourDisplay.getAbsoluteY() - 1);
        mainPanel.add(scrollBar, 9, 1, 1, SLOTS_HEIGHT - 1);
        mainPanel.add(refreshButton, 17, 11, 1, 1);
        refreshButton.setSize(20, 20);
        refreshButton.setIconSize(18);
        refreshButton.setAlignment(HorizontalAlignment.LEFT);
        refreshButton.setOnClick(() -> {
            MCRGBClient.refreshColours();
            colourSort();
        });

        mainPanel.add(searchField, 6, 0, 4, 1);
        searchField.setSize(4 * 18, 11);

        mainPanel.add(settingsButton, 17, 0, 1, 1);
        settingsButton.setSize(20, 20);
        settingsButton.setIconSize(18);
        settingsButton.setAlignment(HorizontalAlignment.LEFT);

        mainPanel.add(rgbButton, 10, 11, 1, 1);
        rgbButton.setLocation(201, 205);
        rgbButton.setSize(26, 20);
        rgbButton.setEnabled(false);
        rgbButton.setAlignment(HorizontalAlignment.CENTER);
        mainPanel.add(hsvButton, 13, 11, 1, 1);
        hsvButton.setLocation(237, 205);
        hsvButton.setSize(26, 20);
        hsvButton.setAlignment(HorizontalAlignment.CENTER);
        mainPanel.add(hslButton, 15, 11, 1, 1);
        hslButton.setLocation(273, 205);
        hslButton.setSize(26, 20);
        hslButton.setAlignment(HorizontalAlignment.CENTER);

        mainPanel.add(new WLabel(Component.translatable("ui.mcrgb_forge.header")), 0, 0, 2, 1);
        mainPanel.add(savedPalettesArea, 0, SLOTS_HEIGHT);

        mainPanel.add(sliderArea, 11, 2, 6, 7);

        mainPanel.add(rLabel, 6, 7, 1, 1);
        mainPanel.add(gLabel, 6, 7, 1, 1);
        mainPanel.add(bLabel, 6, 7, 1, 1);
        rLabel.setLocation(211, 50);
        gLabel.setLocation(247, 50);
        bLabel.setLocation(283, 50);

        sliderArea.add(rSlider, 0, 18, 18, 108);
        rSlider.setValue(inputColour.r);
        sliderArea.add(gSlider, 36, 18, 18, 108);
        gSlider.setValue(inputColour.g);
        sliderArea.add(bSlider, 72, 18, 18, 108);
        bSlider.setValue(inputColour.b);

        mainPanel.add(inputs, 10, 9, 2, 1);
        inputs.add(rInput, 14, 9, 26, 1);
        inputs.add(gInput, 50, 9, 26, 1);
        inputs.add(bInput, 86, 9, 26, 1);

        rSlider.setValueChangeListener((int value) -> {
            if (enableSliderListeners) {
                sliderAdjust('r', value);
            }
        });
        gSlider.setValueChangeListener((int value) -> {
            if (enableSliderListeners) {
                sliderAdjust('g', value);
            }
        });
        bSlider.setValueChangeListener((int value) -> {
            if (enableSliderListeners) {
                sliderAdjust('b', value);
            }
        });

        rSlider.setDraggingFinishedListener((int value) -> {
            if (!MCRGBConfig.SLIDER_CONSTANT_UPDATE.get()) {
                colourSort();
            }
        });
        gSlider.setDraggingFinishedListener((int value) -> {
            if (!MCRGBConfig.SLIDER_CONSTANT_UPDATE.get()) {
                colourSort();
            }
        });
        bSlider.setDraggingFinishedListener((int value) -> {
            if (!MCRGBConfig.SLIDER_CONSTANT_UPDATE.get()) {
                colourSort();
            }
        });

        wheelValueSlider.setValueChangeListener((int value) -> {
            colourWheel.setOpaqueTint(FastColor.ARGB32.color(255, value, value, value));
            colourWheel.pickAtCursor();
        });

        rInput.setChangedListener((String value) -> RGBTyped('r', value));
        gInput.setChangedListener((String value) -> RGBTyped('g', value));
        bInput.setChangedListener((String value) -> RGBTyped('b', value));

        hexInput.setChangedListener((String value) -> hexTyped(value, false));
        searchField.setChangedListener((String value) -> colourSort());

        rgbButton.setOnClick(() -> setColourMode(ColourMode.RGB));
        hsvButton.setOnClick(() -> setColourMode(ColourMode.HSV));
        hslButton.setOnClick(() -> setColourMode(ColourMode.HSL));

        colourWheelToggle.setOnToggle(this::toggleColourWheel);

        if (ModList.get().isLoaded("cloth_config")) {
            settingsButton.setOnClick(() -> Minecraft.getInstance().setScreen(ClothConfigIntegration.getConfigScreen(Minecraft.getInstance().screen)));
        } else {
            settingsButton.setOnClick(() -> Minecraft.getInstance().player.displayClientMessage(Component.translatable("warning.mcrgb_forge.noclothconfig"), false));
        }
        updateArmour();

        mainPanel.add(armourSlots, 17, 3);

        armourSlots.add(new WColourGuiSlot(helmet, this), 0, 0);
        armourSlots.add(new WColourGuiSlot(chestplate, this), 0, 1);
        armourSlots.add(new WColourGuiSlot(leggings, this), 0, 2);
        armourSlots.add(new WColourGuiSlot(boots, this), 0, 3);
        armourSlots.add(new WColourGuiSlot(horse, this), 0, 4);

        colourWheelToggle.setOffImage(new Texture(ResourceLocation.fromNamespaceAndPath(MOD_ID, "wheel_small.png")));
        colourWheelToggle.setOnImage(new Texture(ResourceLocation.fromNamespaceAndPath(MOD_ID, "sliders.png")));
        mainPanel.add(colourWheelToggle, 17, 10);
        colourWheelToggle.setLocation(314, 180);

        setColour(launchColour);
        mainPanel.validate(this);
        root.validate(this);
    }

    public void setColourMode(ColourMode cm) {
        enableSliderListeners = false;
        mode = cm;
        ColourVector colour = new ColourVector(inputColour.getHex());

        switch (mode) {
            case RGB:
                rLabel.setText(Component.translatable("ui.mcrgb_forge.r_for_red"));
                rLabel.setColor(0xFFFF0000);
                gLabel.setText(Component.translatable("ui.mcrgb_forge.g_for_green"));
                gLabel.setColor(0xFF00FF00);
                bLabel.setText(Component.translatable("ui.mcrgb_forge.b_for_blue"));
                bLabel.setColor(0xFF0000FF);

                rSlider.setMinValue(0);
                gSlider.setMinValue(0);
                bSlider.setMinValue(0);
                rSlider.setMaxValue(255);
                gSlider.setMaxValue(255);
                bSlider.setMaxValue(255);
                rgbButton.setEnabled(false);
                hsvButton.setEnabled(true);
                hslButton.setEnabled(true);
                break;
            case HSV:
                rLabel.setText(Component.translatable("ui.mcrgb_forge.h_for_hue_hsv"));
                rLabel.setColor(0xFF3F3F3F);
                gLabel.setText(Component.translatable("ui.mcrgb_forge.s_for_sat_hsv"));
                gLabel.setColor(0xFF3F3F3F);
                bLabel.setText(Component.translatable("ui.mcrgb_forge.v_for_val_hsv"));
                bLabel.setColor(0xFF3F3F3F);
                rSlider.setMinValue(0);
                gSlider.setMinValue(0);
                bSlider.setMinValue(0);
                rSlider.setMaxValue(360);
                gSlider.setMaxValue(100);
                bSlider.setMaxValue(100);
                rgbButton.setEnabled(true);
                hsvButton.setEnabled(false);
                hslButton.setEnabled(true);
                break;
            case HSL:
                rLabel.setText(Component.translatable("ui.mcrgb_forge.h_for_hue_hsl"));
                rLabel.setColor(0xFF3F3F3F);
                gLabel.setText(Component.translatable("ui.mcrgb_forge.s_for_sat_hsl"));
                gLabel.setColor(0xFF3F3F3F);
                bLabel.setText(Component.translatable("ui.mcrgb_forge.l_for_lit_hsl"));
                bLabel.setColor(0xFF3F3F3F);
                rSlider.setMinValue(0);
                gSlider.setMinValue(0);
                bSlider.setMinValue(0);
                rSlider.setMaxValue(360);
                gSlider.setMaxValue(100);
                bSlider.setMaxValue(100);
                rgbButton.setEnabled(true);
                hsvButton.setEnabled(true);
                hslButton.setEnabled(false);
                break;
        }
        hexTyped(colour.getHex(), true);
        inputColour = colour;
        enableSliderListeners = true;
    }

    public void sliderAdjust(char d, int value) {
        switch (mode) {
            case RGB:
                if (d == 'r') {
                    if (inputColour.r == value) {
                        return;
                    }
                    inputColour.r = value;
                    rInput.setText(Integer.toString(inputColour.r));
                }
                if (d == 'g') {
                    if (inputColour.g == value) {
                        return;
                    }
                    inputColour.g = value;
                    gInput.setText(Integer.toString(inputColour.g));
                }
                if (d == 'b') {
                    if (inputColour.b == value) {
                        return;
                    }
                    inputColour.b = value;
                    bInput.setText(Integer.toString(inputColour.b));
                }
                break;
            case HSV:
                inputColour.fromHSV(rSlider.getValue(), gSlider.getValue(), bSlider.getValue());
                rInput.setText(Integer.toString(rSlider.getValue()));
                gInput.setText(Integer.toString(gSlider.getValue()));
                bInput.setText(Integer.toString(bSlider.getValue()));
                break;
            case HSL:
                inputColour.fromHSL(rSlider.getValue(), gSlider.getValue(), bSlider.getValue());
                rInput.setText(Integer.toString(rSlider.getValue()));
                gInput.setText(Integer.toString(gSlider.getValue()));
                bInput.setText(Integer.toString(bSlider.getValue()));
                break;
        }
        hexInput.setText(inputColour.getHex());
        updateArmour();
        if (MCRGBConfig.SLIDER_CONSTANT_UPDATE.get()) {
            colourSort();
        }
    }

    public void RGBTyped(char d, String value) {
        switch (mode) {
            case RGB:
                if (!rInput.isFocused() && !gInput.isFocused() && !bInput.isFocused()) {
                    return;
                }
                if (Integer.parseInt(value) > 255 || Integer.parseInt(value) < 0) {
                    return;
                }

                if (d == 'r') {
                    if (inputColour.r == Integer.parseInt(value)) {
                        return;
                    }
                    inputColour.r = Integer.parseInt(value);
                    rSlider.setValue(inputColour.r);
                }
                if (d == 'g') {
                    if (inputColour.g == Integer.parseInt(value)) {
                        return;
                    }
                    inputColour.g = Integer.parseInt(value);
                    gSlider.setValue(inputColour.g);
                }
                if (d == 'b') {
                    if (inputColour.b == Integer.parseInt(value)) {
                        return;
                    }
                    inputColour.b = Integer.parseInt(value);
                    bSlider.setValue(inputColour.b);
                }
                break;
            case HSV:
                if (!rInput.isFocused() && !gInput.isFocused() && !bInput.isFocused()) {
                    return;
                }
                if (d == 'r') {
                    if (Integer.parseInt(value) > 360 || Integer.parseInt(value) < 0) {
                        return;
                    }
                } else {
                    if (Integer.parseInt(value) > 100 || Integer.parseInt(value) < 0) {
                        return;
                    }
                }
                if (d == 'r') {
                    if (inputColour.r == Integer.parseInt(value)) {
                        return;
                    }
                    rSlider.setValue(Integer.parseInt(value));
                }
                if (d == 'g') {
                    if (inputColour.g == Integer.parseInt(value)) {
                        return;
                    }
                    gSlider.setValue(Integer.parseInt(value));
                }
                if (d == 'b') {
                    if (inputColour.b == Integer.parseInt(value)) {
                        return;
                    }
                    bSlider.setValue(Integer.parseInt(value));
                }
                inputColour.fromHSV(rSlider.getValue(), gSlider.getValue(), bSlider.getValue());
                break;
            case HSL:
                if (!rInput.isFocused() && !gInput.isFocused() && !bInput.isFocused()) {
                    return;
                }
                if (d == 'r') {
                    if (Integer.parseInt(value) > 360 || Integer.parseInt(value) < 0) {
                        return;
                    }
                } else {
                    if (Integer.parseInt(value) > 100 || Integer.parseInt(value) < 0) {
                        return;
                    }
                }

                if (d == 'r') {
                    if (inputColour.r == Integer.parseInt(value)) {
                        return;
                    }
                    rSlider.setValue(Integer.parseInt(value));
                }
                if (d == 'g') {
                    if (inputColour.g == Integer.parseInt(value)) {
                        return;
                    }
                    gSlider.setValue(Integer.parseInt(value));
                }
                if (d == 'b') {
                    if (inputColour.b == Integer.parseInt(value)) {
                        return;
                    }
                    bSlider.setValue(Integer.parseInt(value));
                }
                inputColour.fromHSL(rSlider.getValue(), gSlider.getValue(), bSlider.getValue());
                break;
        }
        hexInput.setText(inputColour.getHex());
        updateArmour();
        if (colourWheelToggle.getToggle()) {
            int val = Math.max(Math.max(inputColour.r, inputColour.g), inputColour.b);
            colourWheel.setOpaqueTint(new ColourVector(val, val, val).asInt());
        }
    }

    public void hexTyped(String value, boolean modeChanged) {
        enableSliderListeners = false;

        ColourVector colour = new ColourVector(value);
        if (!modeChanged && !hexInput.isFocused()) {
            enableSliderListeners = true;
            return;
        }
        if (!modeChanged && value.equals(inputColour.getHex())) {
            enableSliderListeners = true;
            return;
        }
        switch (mode) {
            case RGB:
                rSlider.setValue(colour.r);
                rInput.setText(Integer.toString(colour.r));

                gSlider.setValue(colour.g);
                gInput.setText(Integer.toString(colour.g));

                bSlider.setValue(colour.b);
                bInput.setText(Integer.toString(colour.b));
                break;
            case HSV:
                rSlider.setValue(colour.getHue());
                rInput.setText(Integer.toString(colour.getHue()));

                gSlider.setValue(colour.getSatV());
                gInput.setText(Integer.toString(colour.getSatV()));

                bSlider.setValue(colour.getVal());
                bInput.setText(Integer.toString(colour.getVal()));
                break;
            case HSL:
                rSlider.setValue(colour.getHue());
                rInput.setText(Integer.toString(colour.getHue()));

                gSlider.setValue(colour.getSatL());
                gInput.setText(Integer.toString(colour.getSatL()));

                bSlider.setValue(colour.getLight());
                bInput.setText(Integer.toString(colour.getLight()));
                break;
        }
        inputColour = colour;
        updateArmour();
        colourSort();
        enableSliderListeners = true;
    }

    public void updateArmour() {
        final String DISPLAY = "display";
        final String COLOR = "color";

        int hexInt = getColour();
        helmet.getOrCreateTagElement(DISPLAY).putInt(COLOR, hexInt);
        chestplate.getOrCreateTagElement(DISPLAY).putInt(COLOR, hexInt);
        leggings.getOrCreateTagElement(DISPLAY).putInt(COLOR, hexInt);
        boots.getOrCreateTagElement(DISPLAY).putInt(COLOR, hexInt);
        horse.getOrCreateTagElement(DISPLAY).putInt(COLOR, hexInt);
        colourDisplay.setOpaqueTint(hexInt);
    }

    public void colourSort() {
        stacks.clear();
        ColourVector query = inputColour;

        ForgeRegistries.BLOCKS.forEach(block -> {
            IItemBlockColourSaver itemBlockColourSaver = (IItemBlockColourSaver) block.asItem();

            for (int j = 0; j < itemBlockColourSaver.mcrgb_forge$getLength(); j++) {
                double distance = 0;
                double weightless;
                double weight;
                SpriteDetails sprite = itemBlockColourSaver.mcrgb_forge$getSpriteDetails(j);
                for (int i = 0; i < sprite.getColours().size(); i++) {
                    SpriteColour spriteColour = sprite.getColours().get(i);
                    ColourVector colour = spriteColour.color();
                    weight = spriteColour.weight();

                    if (colour == null) {
                        return;
                    }
                    weightless = query.distance(colour) + 0.000001;
                    if (weightless / Math.pow(weight, 5) < distance || i == 0) {
                        distance = weightless / Math.pow(weight, 5);
                    }
                }

                if (distance < itemBlockColourSaver.mcrgb_forge$getScore() || j == 0) {
                    itemBlockColourSaver.mcrgb_forge$setScore(distance);
                }
            }
            if (block.getName().getString().toUpperCase().contains(searchField.getText().toUpperCase()) && itemBlockColourSaver.mcrgb_forge$getLength() > 0
                    && block.isEnabled(Minecraft.getInstance().level.enabledFeatures())) {
                stacks.add(new ItemStack(block));
            }
        });

        scrollBar.setMaxValue(stacks.size() / SLOTS_WIDTH + SLOTS_WIDTH);
        stacks.sort((is1, is2) -> {
            double x = ((IItemBlockColourSaver) is1.getItem()).mcrgb_forge$getScore();
            double y = ((IItemBlockColourSaver) is2.getItem()).mcrgb_forge$getScore();
            return Double.compare(x, y);
        });
        placeSlots();
    }

    public void placeSlots() {
        wColourGuiSlots.forEach(mainPanel::remove);
        int index = SLOTS_WIDTH * scrollBar.getValue();
        for (int j = 1; j < SLOTS_HEIGHT; j++) {
            for (int i = 0; i < SLOTS_WIDTH; i++) {
                if (index >= stacks.size()) {
                    break;
                }
                WColourGuiSlot colourGuiSlot = new WColourGuiSlot(stacks.get(index), this);

                if (wColourGuiSlots.size() <= index) {
                    wColourGuiSlots.add(colourGuiSlot);
                } else {
                    wColourGuiSlots.set(index, colourGuiSlot);
                }
                mainPanel.add(colourGuiSlot, i, j);
                index++;

            }
        }
        mainPanel.validate(this);
    }

    @Override
    public void setColour(ColourVector colour) {
        switch (mode) {
            case RGB:
                rSlider.setValue(colour.r);
                rInput.setText(Integer.toString(colour.r));

                gSlider.setValue(colour.g);
                gInput.setText(Integer.toString(colour.g));

                bSlider.setValue(colour.b);
                bInput.setText(Integer.toString(colour.b));
                break;
            case HSV:
                rSlider.setValue(colour.getHue());
                rInput.setText(Integer.toString(colour.getHue()));

                gSlider.setValue(colour.getSatV());
                gInput.setText(Integer.toString(colour.getSatV()));

                bSlider.setValue(colour.getVal());
                bInput.setText(Integer.toString(colour.getVal()));
                break;
            case HSL:
                rSlider.setValue(colour.getHue());
                rInput.setText(Integer.toString(colour.getHue()));

                gSlider.setValue(colour.getSatL());
                gInput.setText(Integer.toString(colour.getSatL()));

                bSlider.setValue(colour.getLight());
                bInput.setText(Integer.toString(colour.getLight()));
                break;
        }
        inputColour = colour;
        hexInput.setText(inputColour.getHex());
        updateArmour();
        colourSort();
        scrollBar.setValue(0);
        placeSlots();
    }

    public void openBlockInfoGui(ItemStack stack) {
        Minecraft.getInstance().setScreen(new ColourScreen(new BlockInfoGui(stack, inputColour)));
    }

    public void toggleColourWheel(boolean isToggled) {
        if (isToggled) {
            mainPanel.remove(sliderArea);

            //Remove and re-add inputs to workaround visual bug in 1.20.1 only
            mainPanel.remove(rLabel);
            mainPanel.remove(gLabel);
            mainPanel.remove(bLabel);
            mainPanel.add(rLabel, 1, 1, 1, 1);
            mainPanel.add(gLabel, 1, 1, 1, 1);
            mainPanel.add(bLabel, 1, 1, 1, 1);
            mainPanel.remove(inputs);
            mainPanel.add(inputs, 10, 9, 2, 1);

            mainPanel.remove(armourSlots);
            mainPanel.add(colourWheel, 11, 2, 6, 6);
            mainPanel.add(wheelValueSlider, 17, 2, 1, 6);
            wheelValueSlider.setValue(wheelValueSlider.getMaxValue());
            colourWheel.setLocation(198, 47);
            wheelValueSlider.setLocation(314, 47);
            wheelValueSlider.setSize(18, 128);
            rLabel.setLocation(211, 165);
            gLabel.setLocation(247, 165);
            bLabel.setLocation(283, 165);
        } else {
            mainPanel.add(sliderArea, 11, 2, 6, 7);
            mainPanel.add(armourSlots, 17, 3);
            mainPanel.remove(colourWheel);
            mainPanel.remove(wheelValueSlider);
            rLabel.setLocation(211, 50);
            gLabel.setLocation(247, 50);
            bLabel.setLocation(283, 50);

        }
        root.validate(this);
    }

    public enum ColourMode {
        RGB, HSV, HSL
    }
}
