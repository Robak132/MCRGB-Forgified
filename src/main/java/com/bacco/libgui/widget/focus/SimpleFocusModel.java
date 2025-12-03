package com.bacco.libgui.widget.focus;

import com.bacco.libgui.widget.WWidget;
import com.bacco.libgui.widget.data.Rect2i;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

record SimpleFocusModel(WWidget widget, Rect2i area) implements FocusModel<@Nullable Void> {
	@Override
	public boolean isFocused(Focus<@Nullable Void> focus) {
		return widget.isFocused();
	}

	@Override
	public void setFocused(Focus<@Nullable Void> focusArea) {
	}

	@Override
	public Stream<Focus<@Nullable Void>> foci() {
		return Stream.of(Focus.of(area));
	}
}
