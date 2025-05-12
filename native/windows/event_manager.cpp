#include "event_manager_jni.h"
#include <vector>
#include <string>
#include <jni.h>
#include "jni_utils.h"
#include <windows.h>
#include <cstdint>
#include <unordered_map>

#include <Xinput.h>
#include <thread>
#include <chrono>

#pragma comment(lib, "xinput.lib")

std::map<HWND, WNDPROC> wndProcMap;

// Custom Window Procedure
LRESULT CALLBACK CustomWndProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    if (uMsg == WM_POWERBROADCAST) {
        // System is about to suspend (sleep)
        JNIEnv* env;
        jvm->AttachCurrentThread((void**)&env, NULL);

        if (wParam == PBT_APMSUSPEND) {
            // WillSuspend
            submitSuspendStateToJava(env, SuspendState::WillSuspend);
        } else if (wParam == PBT_APMRESUMEAUTOMATIC) {
            // WillResume (automatic, maybe no user input yet)
            submitSuspendStateToJava(env, SuspendState::WillResume);
        } else if (wParam == PBT_APMRESUMESUSPEND) {
            // DidResume (user is now active)
            submitSuspendStateToJava(env, SuspendState::DidResume);
        }
    }
    return DefWindowProc(hwnd, uMsg, wParam, lParam);
}

void pollGamepads(JNIEnv* env) {
    DWORD connected[4] = {0};

    while (true) {
        for (DWORD i = 0; i < XUSER_MAX_COUNT; ++i) {
            XINPUT_STATE state;
            ZeroMemory(&state, sizeof(XINPUT_STATE));

            DWORD result = XInputGetState(i, &state);
            if (result == ERROR_SUCCESS) {
                if (!connected[i]) {
                    connected[i] = 1;
                    sendGamepadConnectedEvent(env, i);
                }

                // Normalize thumbstick values to [-1.0, 1.0]
                float lx = state.Gamepad.sThumbLX / 32767.0f;
                float ly = state.Gamepad.sThumbLY / 32767.0f;
                float rx = state.Gamepad.sThumbRX / 32767.0f;
                float ry = state.Gamepad.sThumbRY / 32767.0f;

                // Normalize trigger values to [0.0, 1.0]
                float lt = state.Gamepad.bLeftTrigger / 255.0f;
                float rt = state.Gamepad.bRightTrigger / 255.0f;

                int32_t gamepdId = static_cast<int32_t>(i);
                sendAxisEvent(env, gamepdId, 0, lx); // Left Thumbstick X
                sendAxisEvent(env, gamepdId, 1, ly); // Left Thumbstick Y
                sendAxisEvent(env, gamepdId, 2, rx); // Right Thumbstick X
                sendAxisEvent(env, gamepdId, 3, ry); // Right Thumbstick Y
                sendAxisEvent(env, gamepdId, 4, lt); // Left Trigger
                sendAxisEvent(env, gamepdId, 5, rt); // Right Trigger
            } else {
                if (connected[i]) {
                    connected[i] = 0;
                    sendGamepadDisconnectedEvent(env, i);
                }
            }
        }
        std::this_thread::sleep_for(std::chrono::milliseconds(16)); // ~60Hz
    }
}
