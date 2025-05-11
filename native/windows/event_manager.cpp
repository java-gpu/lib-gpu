#include "event_manager_jni.h"
#include <vector>
#include <string>
#include <jni.h>
#include "jni_utils.h"

bool pollEventFromSystem(void* windowHandler, NativeUiEvent* evt) {
    // Fill in evt's fields as appropriate
    // e.g. evt->type = Axis;
    return true;
}