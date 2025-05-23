/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
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

package tech.gpu.lib.maps.tiled;

import tech.gpu.lib.graphics.PixelFormat;
import tech.gpu.lib.graphics.g2d.TextureRegion;
import tech.gpu.lib.maps.MapLayer;

public class TiledMapImageLayer extends MapLayer {

    private TextureRegion region;

    private float x;
    private float y;
    private boolean repeatX;
    private boolean repeatY;
    private boolean supportsTransparency;

    public TiledMapImageLayer(TextureRegion region, float x, float y, boolean repeatX, boolean repeatY) {
        this.region = region;
        this.x = x;
        this.y = y;
        this.repeatX = repeatX;
        this.repeatY = repeatY;
        this.supportsTransparency = checkTransparencySupport(region);
    }

    /**
     * TiledMap ImageLayers can support transparency through tint color if the image provided supports the proper pixel format.
     * Here we check to see if the file supports transparency by checking the format of the TextureData.
     *
     * @param region TextureRegion of the ImageLayer
     * @return boolean
     */
    private boolean checkTransparencySupport(TextureRegion region) {
        PixelFormat format = region.getTexture().getFormat();
        return format != null && formatHasAlpha(format);
    }

    // Check if pixel format supports alpha channel
    private boolean formatHasAlpha(PixelFormat format) {
        switch (format) {
            case Alpha:
            case LuminanceAlpha:
            case RGBA4444:
            case RGBA8888:
                return true;
            default:
                return false;
        }
    }

    public boolean supportsTransparency() {
        return supportsTransparency;
    }

    public TextureRegion getTextureRegion() {
        return region;
    }

    public void setTextureRegion(TextureRegion region) {
        this.region = region;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isRepeatX() {
        return repeatX;
    }

    public void setRepeatX(boolean repeatX) {
        this.repeatX = repeatX;
    }

    public boolean isRepeatY() {
        return repeatY;
    }

    public void setRepeatY(boolean repeatY) {
        this.repeatY = repeatY;
    }

}
