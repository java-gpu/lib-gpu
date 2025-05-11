package tech.lib.ui.input;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.util.StringUtils;
import tech.lib.ui.data.RingBufferControl;
import tech.lib.ui.enu.KeyEnum;
import tech.lib.ui.ex.UiProcessingException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class InputKeyboard {
    private static final int NUMBER_OF_BYTE_PER_CHAR = 4;
    private static final int DEFAULT_RING_SIZE = 256;
    private Map<KeyEnum, KeyState> keys;
    private Map<KeyEnum, Boolean> once;

    private RingBufferControl ring;

    public InputKeyboard() {
        ring = new RingBufferControl((DEFAULT_RING_SIZE - 1) * NUMBER_OF_BYTE_PER_CHAR);
        reset();
    }

    public void reset() {
        keys = new HashMap<>();
        once = new HashMap<>();
        for (KeyEnum key : KeyEnum.values()) {
            keys.put(key, decodeKeyState(0));
            once.put(key, false);
        }
    }

    /**
     * Bit Layout<br/>
     * Bits 24–31: unused (0) <br>
     * Bits 16–23: modifier keys (only if key is pressed) <br>
     * Bits 8–15 : 1 if key is down, 0 if up. <br>
     * Bits 0–7  : unused (0). <br>
     *
     * @param _modifiers Modifier
     * @param _down      Is pressed down or not
     * @return Encodes the state of a keyboard key event (modifiers + whether the key is pressed) into a single ong value using bitwise operations.
     */
    public static KeyState encodeKeyState(int _modifiers, boolean _down) {
        return encodeKeyState(new KeyState(_modifiers, _down));
    }

    /**
     * Bit Layout<br/>
     * Bits 24–31: unused (0) <br>
     * Bits 16–23: modifier keys (only if key is pressed) <br>
     * Bits 8–15 : 1 if key is down, 0 if up. <br>
     * Bits 0–7  : unused (0). <br>
     *
     * @param keyState The KeyState which stored modifier and down state
     * @return Set encoded value to the keyState and return itself.
     * Encodes the state of a keyboard key event (modifiers + whether the key is pressed) into a single ong value using bitwise operations.
     */
    public static KeyState encodeKeyState(KeyState keyState) {
        long state = 0;
        state |= (long) (keyState.down ? keyState.modifier : 0) << 16;
        state |= (keyState.down ? 1L : 0) << 8;
        keyState.setEncoded(state);
        return keyState;
    }

    /**
     * See #encodeKeyState for reference.
     *
     * @param state bitwise state value
     * @return Key state
     */
    public static KeyState decodeKeyState(long state) {
        int _modifiers = (int) ((state >> 16) & 0xff);
        var result = 0 != ((state >> 8) & 0xff);
        return new KeyState(_modifiers, result, state);
    }

    public void setKeyState(KeyEnum _key, int _modifiers, boolean _down) {
        keys.put(_key, encodeKeyState(_modifiers, _down));
        once.put(_key, false);
    }

    public KeyState getKeyState(KeyEnum _key) {
        return decodeKeyState(keys.get(_key).encoded);
    }

    public int getModifiersState() {
        int modifiers = 0;
        for (KeyEnum key : KeyEnum.values()) {
            modifiers |= (int) ((keys.get(key).encoded >> 16) & 0xff);
        }
        return modifiers;
    }

    public void pushChar(int _len, char _char) {
        for (var i = 0; i < _len && ring.reserve(NUMBER_OF_BYTE_PER_CHAR); i++) {
            var popResult = popChar();
            if (popResult == null) {
                break;
            }
        }
        int writeResult = ring.write(StringUtils.getInstance().encodeToFixed4Bytes(_char));
        if (writeResult <= 0) {
            throw new UiProcessingException("RingBufferControl write fail!");
        }
    }

    public Character popChar() {
        int availableToRead = ring.availableToRead();
        if (availableToRead > 0) {
            byte[] readChar = new byte[NUMBER_OF_BYTE_PER_CHAR];
            int read = ring.read(readChar, readChar.length);
            if (read < NUMBER_OF_BYTE_PER_CHAR) {
                log.debug("Warning!! Called popChar to read {} bytes but only can read {} bytes", NUMBER_OF_BYTE_PER_CHAR, read);
            }
            String decoded = new String(readChar, StandardCharsets.UTF_8);
            return decoded.charAt(0);
        }

        return null;
    }

    public void charFlush() {
        ring.reset();
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class KeyState {
        private int modifier;
        private boolean down;
        @Setter
        private long encoded;

        public KeyState(int _modifiers, boolean _down) {
            this(_modifiers, _down, -1);
        }
    }
}
