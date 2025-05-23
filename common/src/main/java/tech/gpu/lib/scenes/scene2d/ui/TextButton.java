/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib.scenes.scene2d.ui;

import tech.gpu.lib.graphics.Color;
import tech.gpu.lib.graphics.g2d.Batch;
import tech.gpu.lib.graphics.g2d.BitmapFont;
import tech.gpu.lib.scenes.scene2d.ui.Label.LabelStyle;
import tech.gpu.lib.scenes.scene2d.utils.Drawable;
import tech.gpu.lib.utils.Align;


/**
 * A button with a child {@link Label} to display text.
 *
 * @author Nathan Sweet
 */
public class TextButton extends Button {
    private Label label;
    private TextButtonStyle style;

    public TextButton(String text, Skin skin) {
        this(text, skin.get(TextButtonStyle.class));
        setSkin(skin);
    }

    public TextButton(String text, Skin skin, String styleName) {
        this(text, skin.get(styleName, TextButtonStyle.class));
        setSkin(skin);
    }

    public TextButton(String text, TextButtonStyle style) {
        super();
        setStyle(style);
        label = newLabel(text, new LabelStyle(style.font, style.fontColor));
        label.setAlignment(Align.center);
        add(label).grow();
        setSize(getPrefWidth(), getPrefHeight());
    }

    protected Label newLabel(String text, LabelStyle style) {
        return new Label(text, style);
    }

    public void setStyle(ButtonStyle style) {
        if (style == null) throw new NullPointerException("style cannot be null");
        if (!(style instanceof TextButtonStyle)) throw new IllegalArgumentException("style must be a TextButtonStyle.");
        this.style = (TextButtonStyle) style;
        super.setStyle(style);

        if (label != null) {
            TextButtonStyle textButtonStyle = (TextButtonStyle) style;
            LabelStyle labelStyle = label.getStyle();
            labelStyle.font = textButtonStyle.font;
            labelStyle.fontColor = textButtonStyle.fontColor;
            label.setStyle(labelStyle);
        }
    }

    public TextButtonStyle getStyle() {
        return style;
    }

    /**
     * Returns the appropriate label font color from the style based on the current button state.
     */
    protected Color getFontColor() {
        if (isDisabled() && style.disabledFontColor != null) return style.disabledFontColor;
        if (isPressed()) {
            if (isChecked() && style.checkedDownFontColor != null) return style.checkedDownFontColor;
            if (style.downFontColor != null) return style.downFontColor;
        }
        if (isOver()) {
            if (isChecked()) {
                if (style.checkedOverFontColor != null) return style.checkedOverFontColor;
            } else {
                if (style.overFontColor != null) return style.overFontColor;
            }
        }
        boolean focused = hasKeyboardFocus();
        if (isChecked()) {
            if (focused && style.checkedFocusedFontColor != null) return style.checkedFocusedFontColor;
            if (style.checkedFontColor != null) return style.checkedFontColor;
            if (isOver() && style.overFontColor != null) return style.overFontColor;
        }
        if (focused && style.focusedFontColor != null) return style.focusedFontColor;
        return style.fontColor;
    }

    public void draw(Batch batch, float parentAlpha) {
        label.getStyle().fontColor = getFontColor();
        super.draw(batch, parentAlpha);
    }

    public void setLabel(Label label) {
        if (label == null) throw new IllegalArgumentException("label cannot be null.");
        getLabelCell().setActor(label);
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    public Cell<Label> getLabelCell() {
        return getCell(label);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public CharSequence getText() {
        return label.getText();
    }

    public String toString() {
        String name = getName();
        if (name != null) return name;
        String className = getClass().getName();
        int dotIndex = className.lastIndexOf('.');
        if (dotIndex != -1) className = className.substring(dotIndex + 1);
        return (className.indexOf('$') != -1 ? "TextButton " : "") + className + ": " + label.getText();
    }

    /**
     * The style for a text button, see {@link TextButton}.
     *
     * @author Nathan Sweet
     */
    static public class TextButtonStyle extends ButtonStyle {
        public BitmapFont font;
        public Color fontColor, downFontColor, overFontColor, focusedFontColor, disabledFontColor;
        public Color checkedFontColor, checkedDownFontColor, checkedOverFontColor, checkedFocusedFontColor;

        public TextButtonStyle() {
        }

        public TextButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked);
            this.font = font;
        }

        public TextButtonStyle(TextButtonStyle style) {
            super(style);
            font = style.font;

            if (style.fontColor != null) fontColor = new Color(style.fontColor);
            if (style.downFontColor != null) downFontColor = new Color(style.downFontColor);
            if (style.overFontColor != null) overFontColor = new Color(style.overFontColor);
            if (style.focusedFontColor != null) focusedFontColor = new Color(style.focusedFontColor);
            if (style.disabledFontColor != null) disabledFontColor = new Color(style.disabledFontColor);

            if (style.checkedFontColor != null) checkedFontColor = new Color(style.checkedFontColor);
            if (style.checkedDownFontColor != null) checkedDownFontColor = new Color(style.checkedDownFontColor);
            if (style.checkedOverFontColor != null) checkedOverFontColor = new Color(style.checkedOverFontColor);
            if (style.checkedFocusedFontColor != null)
                checkedFocusedFontColor = new Color(style.checkedFocusedFontColor);
        }
    }
}
