package com.bacco.libgui.client;

import com.bacco.libgui.GuiDescription;
import com.bacco.libgui.widget.WWidget;
import org.jetbrains.annotations.Nullable;

public interface CottonScreenImpl {
	GuiDescription getDescription();

	@Nullable
	WWidget getLastResponder();

	void setLastResponder(@Nullable WWidget lastResponder);
}
