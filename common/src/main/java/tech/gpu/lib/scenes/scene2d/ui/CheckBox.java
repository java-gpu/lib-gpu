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
import tech.gpu.lib.scenes.scene2d.utils.Drawable;
import tech.gpu.lib.utils.Align;

import tech.gpu.lib.utils.Scaling;

/**
 * A checkbox is a button that contains an image indicating the checked or unchecked state and a label.
 *
 * @author Nathan Sweet
 */
public class CheckBox extends TextButton {
    private Image image;
    private Cell imageCell;
    private CheckBoxStyle style;

    public CheckBox(String text, Skin skin) {
        this(text, skin.get(CheckBoxStyle.class));
    }

    public CheckBox(String text, Skin skin, String styleName) {
        this(text, skin.get(styleName, CheckBoxStyle.class));
    }

    public CheckBox(String text, CheckBoxStyle style) {
        super(text, style);

        Label label = getLabel();
        label.setAlignment(Align.left);

        image = newImage();
        image.setDrawable(style.checkboxOff);

        clearChildren();
        imageCell = add(image);
        add(label);
        setSize(getPrefWidth(), getPrefHeight());
    }

    protected Image newImage() {
        return new Image((Drawable) null, Scaling.none);
    }

    public void setStyle(ButtonStyle style) {
        if (!(style instanceof CheckBoxStyle)) throw new IllegalArgumentException("style must be a CheckBoxStyle.");
        this.style = (CheckBoxStyle) style;
        super.setStyle(style);
    }

    /**
     * Returns the checkbox's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
     * called.
     */
    public CheckBoxStyle getStyle() {
        return style;
    }

    public void draw(Batch batch, float parentAlpha) {
        image.setDrawable(getImageDrawable());
        super.draw(batch, parentAlpha);
    }

    protected Drawable getImageDrawable() {
        if (isDisabled()) {
            if (isChecked && style.checkboxOnDisabled != null) return style.checkboxOnDisabled;
            return style.checkboxOffDisabled;
        }
        boolean over = isOver() && !isDisabled();
        if (isChecked && style.checkboxOn != null)
            return over && style.checkboxOnOver != null ? style.checkboxOnOver : style.checkboxOn;
        if (over && style.checkboxOver != null) return style.checkboxOver;
        return style.checkboxOff;
    }

    public Image getImage() {
        return image;
    }

    public Cell getImageCell() {
        return imageCell;
    }

    /**
     * The style for a select box, see {@link CheckBox}.
     *
     * @author Nathan Sweet
     */
    static public class CheckBoxStyle extends TextButtonStyle {
        public Drawable checkboxOn, checkboxOff;
        public Drawable checkboxOnOver, checkboxOver, checkboxOnDisabled, checkboxOffDisabled;

        public CheckBoxStyle() {
        }

        public CheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn, BitmapFont font, Color fontColor) {
            this.checkboxOff = checkboxOff;
            this.checkboxOn = checkboxOn;
            this.font = font;
            this.fontColor = fontColor;
        }

        public CheckBoxStyle(CheckBoxStyle style) {
            super(style);
            checkboxOff = style.checkboxOff;
            checkboxOn = style.checkboxOn;

            checkboxOnOver = style.checkboxOnOver;
            checkboxOver = style.checkboxOver;
            checkboxOnDisabled = style.checkboxOnDisabled;
            checkboxOffDisabled = style.checkboxOffDisabled;
        }
    }
}
