#include "jni_utils.h"
#include <jni.h>
#include <cstdio>

void jniLog(JNIEnv* env, const char* level, const char* fileName, const char* message) {
    // 1. Find the logger class
    jclass loggerClass = env->FindClass("tech/lib/bgfx/util/JniLogger");
    if (loggerClass == nullptr) {
        printf("❌ Failed to find loggerClass JniLogger\n");
        return;
    }

    // 2. Find the singleton method ID
    jmethodID getInstanceMethod = env->GetStaticMethodID(
        loggerClass,
        "getInstance",
        "()Ltech/lib/bgfx/util/JniLogger;"
    );
    if (getInstanceMethod == nullptr) {
        printf("❌ Failed to find getInstance method in JniLogger\n");
        return;
    }

    // 3. Get logger instance
    jobject loggerInstance = env->CallStaticObjectMethod(loggerClass, getInstanceMethod);

    // 4. Find the method ID
    jmethodID logMethod = env->GetMethodID(
        loggerClass,
        "log",
        "(Ltech/lib/bgfx/jni/JniLogData$LogLevel;Ljava/lang/String;Ljava/lang/String;)V"
    );

    // 5. Get LogLevel enum value
    jclass logLevelClass = env->FindClass("tech/lib/bgfx/jni/JniLogData$LogLevel");
    jfieldID levelField = env->GetStaticFieldID(logLevelClass, level, "Ltech/lib/bgfx/jni/JniLogData$LogLevel;");
    jobject logLevelObj = env->GetStaticObjectField(logLevelClass, levelField);

    // 6. Convert fileName and message to jstring
    jstring fileNameStr = env->NewStringUTF(fileName);
    jstring messageStr = env->NewStringUTF(message);

    // 7. Call the method
    env->CallVoidMethod(loggerInstance, logMethod, logLevelObj, fileNameStr, messageStr);
}