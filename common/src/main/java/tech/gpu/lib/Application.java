/*******************************************************************************
 * This class modified from original libgdx
 * Copyright 2025 See AUTHORS file.
 *<p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *</p>
 *<p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *</p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib;

import tech.gpu.lib.utils.Clipboard;

/**
 * <p>
 * An <code>Application</code> is the main entry point of your project. It sets up a window and rendering surface and manages the
 * different aspects of your application, namely {@link Graphics}, {@link Input}, and {@link Audio}. Think of an
 * Application being equivalent to Swing's <code>JFrame</code> or Android's <code>Activity</code>.
 * </p>
 *
 * <p>
 * An application can be an instance of any of the following:
 * <p>
 * Each application class has it's own startup and initialization methods. Please refer to their documentation for more
 * information.
 * </p>
 *
 * <p>
 * While game programmers are used to having a main loop, libGpu employs a different concept to accommodate the event based nature
 * of Android applications a little more. You application logic must be implemented in a {@link ApplicationListener} which has
 * methods that get called by the Application when the application is created, resumed, paused, disposed or rendered. As a
 * developer you will simply implement the ApplicationListener interface and fill in the functionality accordingly. The
 * ApplicationListener is provided to a concrete Application instance as a parameter to the constructor or another initialization
 * method. Please refer to the documentation of the Application implementations for more information. Note that the
 * ApplicationListener can be provided to any Application implementation. This means that you only need to write your program
 * logic once and have it run on different platforms by passing it to a concrete Application implementation.
 * </p>
 *
 * <p>
 * The Application interface provides you with a set of modules for graphics, audio, input and file i/o.
 * </p>
 */
public interface Application {

    int LOG_NONE = 0;
    int LOG_DEBUG = 3;
    int LOG_INFO = 2;
    int LOG_ERROR = 1;

    /**
     * @return the {@link ApplicationListener} instance
     */
    ApplicationListener getApplicationListener();

    /**
     * @return the {@link Graphics} instance
     */
    Graphics getGraphics();

    /**
     * @return the {@link Audio} instance
     */
    Audio getAudio();

    /**
     * @return the {@link Input} instance
     */
    Input getInput();

    /**
     * Logs a message to the console or logcat
     */
    void log(String tag, String message);

    /**
     * Logs a message to the console or logcat
     */
    void log(String tag, String message, Throwable exception);

    /**
     * Logs an error message to the console or logcat
     */
    void error(String tag, String message);

    /**
     * Logs an error message to the console or logcat
     */
    void error(String tag, String message, Throwable exception);

    /**
     * Logs a debug message to the console or logcat
     */
    void debug(String tag, String message);

    /**
     * Logs a debug message to the console or logcat
     */
    void debug(String tag, String message, Throwable exception);

    /**
     * Sets the log level. {@link #LOG_NONE} will mute all log output. {@link #LOG_ERROR} will only let error messages through.
     * {@link #LOG_INFO} will let all non-debug messages through, and {@link #LOG_DEBUG} will let all messages through.
     *
     * @param logLevel {@link #LOG_NONE}, {@link #LOG_ERROR}, {@link #LOG_INFO}, {@link #LOG_DEBUG}.
     */
    void setLogLevel(int logLevel);

    /**
     * Gets the log level.
     */
    int getLogLevel();

    /**
     * Sets the current Application logger. Calls to {@link #log(String, String)} are delegated to this
     * {@link ApplicationLogger}
     */
    void setApplicationLogger(ApplicationLogger applicationLogger);

    /**
     * @return the current {@link ApplicationLogger}
     */
    ApplicationLogger getApplicationLogger();

    /**
     * @return what {@link Graphics.GraphicsType} this application has, e.g. Android or Desktop
     */
    Graphics.GraphicsType getType();

    /**
     * @return the Android API level on Android, the major OS version on iOS (5, 6, 7, ..), or 0 on the desktop.
     */
    int getVersion();

    /**
     * @return the Java heap memory use in bytes
     */
    long getJavaHeap();

    /**
     * @return the Native heap memory use in bytes
     */
    long getNativeHeap();

    /**
     * Returns the {@link Preferences} instance of this Application. It can be used to store application settings across runs.
     *
     * @param name the name of the preferences, must be useable as a file name.
     * @return the preferences.
     */
    Preferences getPreferences(String name);

    Clipboard getClipboard();

    /**
     * Posts a {@link Runnable} on the main loop thread.
     *
     * @param runnable the runnable.
     */
    void postRunnable(Runnable runnable);

    /**
     * Schedule an exit from the application. On android, this will cause a call to pause() and dispose() some time in the future,
     * it will not immediately finish your application. On iOS this should be avoided in production as it breaks Apples
     * guidelines
     */
    void exit();

    /**
     * Adds a new {@link LifecycleListener} to the application. This can be used by extensions to hook into the lifecycle more
     * easily. The {@link ApplicationListener} methods are sufficient for application level development.
     *
     * @param listener LifecycleListener to be added.
     */
    void addLifecycleListener(LifecycleListener listener);

    /**
     * Removes the {@link LifecycleListener}.
     *
     * @param listener LifecycleListener to be removed.
     */
    void removeLifecycleListener(LifecycleListener listener);
}
