package com.bacco.libgui.client;

import com.bacco.libgui.widget.WPanel;
import com.bacco.libgui.widget.WWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.Stream;

public final class NarrationHelper {
    public static void addNarrations(WPanel rootPanel, NarrationElementOutput builder) {
        List<WWidget> narratableWidgets = getAllWidgets(rootPanel)
          .filter(WWidget::isNarratable)
          .toList();

        for (int i = 0, childCount = narratableWidgets.size(); i < childCount; i++) {
            WWidget child = narratableWidgets.get(i);
            if (!child.isFocused() && !child.isHovered()) continue;

            if (narratableWidgets.size() > 1) {
                builder.add(NarratedElementType.POSITION, Component.translatable(NarrationMessages.Vanilla.SCREEN_POSITION_KEY, i + 1, childCount));

                if (child.isFocused()) {
                    builder.add(NarratedElementType.USAGE, NarrationMessages.Vanilla.COMPONENT_LIST_USAGE);
                }
            }

            child.addNarrations(builder.nest());
        }
    }

    private static Stream<WWidget> getAllWidgets(WPanel panel) {
        return Stream.concat(Stream.of(panel), panel.streamChildren().flatMap(widget -> {
            if (widget instanceof WPanel nested) {
                return getAllWidgets(nested);
            }

            return Stream.of(widget);
        }));
    }
}
