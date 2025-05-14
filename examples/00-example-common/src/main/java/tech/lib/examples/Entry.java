package tech.lib.examples;

import lombok.extern.slf4j.Slf4j;
import tech.lib.bgfx.enu.AppConst;
import tech.lib.bgfx.enu.BgfxDebugFlag;
import tech.lib.bgfx.enu.BgfxResetFlag;
import tech.lib.bgfx.jni.Bgfx;
import tech.lib.ui.event.AppEvent;
import tech.lib.ui.event.CustomMouseEvent;
import tech.lib.ui.input.Input;
import tech.lib.ui.jni.EventManager;

import java.io.File;

@Slf4j
public class Entry {

    private BgfxDebugFlag debug = BgfxDebugFlag.NONE;
    private BgfxResetFlag s_reset = BgfxResetFlag.NONE;
    private int width = AppConst.ENTRY_DEFAULT_WIDTH;
    private int height = AppConst.ENTRY_DEFAULT_HEIGHT;
    private boolean s_exit = false;
    private long windowHandler;
    private Input input;
    private String currentDir;

    public Entry(long windowHandler) {
        this.windowHandler = windowHandler;
        this.input = new Input(windowHandler);
        this.currentDir = new File("./").getAbsolutePath();
    }
//
//    static bx::FileReaderI* s_fileReader = NULL;
//    static bx::FileWriterI* s_fileWriter = NULL;
//
//    extern bx::AllocatorI* getDefaultAllocator();
//    bx::AllocatorI* g_allocator = getDefaultAllocator();
//
//    typedef bx::StringT<&g_allocator> String;

    public boolean processEvents(int width, int height, BgfxDebugFlag _debug, BgfxResetFlag _reset, CustomMouseEvent mouseState) {
        boolean needReset = s_reset != _reset;

        debug = _debug;
        s_reset = _reset;

        // TODO
        boolean mouseLock = input.inputIsMouseLocked();
        AppEvent ev = null;
        do {
            ev = EventManager.pollUiEvent(input.getWindowHandler());

            if (null != ev) {
                switch (ev.getEventType()) {
//                    case Event::Axis:
//                    {
//						const AxisEvent* axis = static_cast<const AxisEvent*>(ev);
//                        inputSetGamepadAxis(axis->m_gamepad, axis->m_axis, axis->m_value);
//                    }
//                    break;
//
//                    case Event::Char:
//                    {
//						const CharEvent* chev = static_cast<const CharEvent*>(ev);
//                        inputChar(chev->m_len, chev->m_char);
//                    }
//                    break;
//
//                    case Event::Exit:
//                        return true;
//
//                    case Event::Gamepad:
//                    {
////						const GamepadEvent* gev = static_cast<const GamepadEvent*>(ev);
////						DBG("gamepad %d, %d", gev->m_gamepad.idx, gev->m_connected);
//                    }
//                    break;
//
//                    case Event::Mouse:
//                    {
//						const MouseEvent* mouse = static_cast<const MouseEvent*>(ev);
//                        handle = mouse->m_handle;
//
//                        inputSetMousePos(mouse->m_mx, mouse->m_my, mouse->m_mz);
//                        if (!mouse->m_move)
//                        {
//                            inputSetMouseButtonState(mouse->m_button, mouse->m_down);
//                        }
//
//                        if (NULL != _mouse
//                            &&  !mouseLock)
//                        {
//                            _mouse->m_mx = mouse->m_mx;
//                            _mouse->m_my = mouse->m_my;
//                            _mouse->m_mz = mouse->m_mz;
//                            if (!mouse->m_move)
//                            {
//                                _mouse->m_buttons[mouse->m_button] = mouse->m_down;
//                            }
//                        }
//                    }
//                    break;
//
//                    case Event::Key:
//                    {
//						const KeyEvent* key = static_cast<const KeyEvent*>(ev);
//                        handle = key->m_handle;
//
//                        inputSetKeyState(key->m_key, key->m_modifiers, key->m_down);
//                    }
//                    break;
//
//                    case Event::Size:
//                    {
//						const SizeEvent* size = static_cast<const SizeEvent*>(ev);
//                        WindowState& win = s_window[0];
//                        win.m_handle = size->m_handle;
//                        win.m_width  = size->m_width;
//                        win.m_height = size->m_height;
//
//                        handle  = size->m_handle;
//                        _width  = size->m_width;
//                        _height = size->m_height;
//                        BX_TRACE("Window resize event: %d: %dx%d", handle, _width, _height);
//
//                        needReset = true;
//                    }
//                    break;
//
//                    case Event::Window:
//                        break;
//
//                    case Event::Suspend:
//                        break;
//
//                    case Event::DropFile:
//                    {
//						const DropFileEvent* drop = static_cast<const DropFileEvent*>(ev);
//                        DBG("%s", drop->m_filePath.getCPtr() );
//                    }
//                    break;
//
//                    default:
//                        break;
                }
            }
//
//            inputProcess();
//
        } while (ev != null);

        needReset |= _reset != s_reset;

        if (windowHandler == 0 && needReset) {
            _reset = s_reset;
            log.debug("bgfx::reset({}, {}, {})", width, height, _reset);
            Bgfx.reset(width, height, _reset);

            inputSetMouseResolution(width, height);
        }

        this.width = width;
        this.height = height;

        return s_exit;
    }

    private void inputSetMouseResolution(int width, int height) {
        input.getMouseInput().setResolution(width, height);
    }
}
