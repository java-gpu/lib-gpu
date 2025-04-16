/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <br/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <br/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <br/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib.audio;

import tech.gpu.lib.Audio;
import tech.gpu.lib.util.Disposable;

import java.io.File;

/**
 * <p>
 * A Sound is a short audio clip that can be played numerous times in parallel. It's completely loaded into memory so only load
 * small audio files. Call the {@link #dispose()} method when you're done using the Sound.
 * </p>
 *
 * <p>
 * Sound instances are created via a call to {@link Audio#newSound(File)}.
 * </p>
 *
 * <p>
 * Calling the {@link #play()} or {@link #play(float)} method will return a long which is an id to that instance of the sound. You
 * can use this id to modify the playback of that sound instance.
 * </p>
 *
 * <p>
 * <b>Note</b>: any values provided will not be clamped, it is the developer's responsibility to do so
 * </p>
 *
 * @author badlogicgames@gmail.com
 */
public interface Sound extends Disposable {
    /**
     * Plays the sound. If the sound is already playing, it will be played again, concurrently.
     *
     * @return the id of the sound instance if successful, or -1 on failure.
     */
    long play();

    /**
     * Plays the sound. If the sound is already playing, it will be played again, concurrently.
     *
     * @param volume the volume in the range [0,1]
     * @return the id of the sound instance if successful, or -1 on failure.
     */
    long play(float volume);

    /**
     * Plays the sound. If the sound is already playing, it will be played again, concurrently. Note that (with the exception of
     * the web backend) panning only works for mono sounds, not for stereo sounds!
     *
     * @param volume the volume in the range [0,1]
     * @param pitch  the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
     * @param pan    panning in the range -1 (full left) to 1 (full right). 0 is center position.
     * @return the id of the sound instance if successful, or -1 on failure.
     */
    long play(float volume, float pitch, float pan);

    /**
     * Plays the sound, looping. If the sound is already playing, it will be played again, concurrently.
     *
     * @return the id of the sound instance if successful, or -1 on failure.
     */
    long loop();

    /**
     * Plays the sound, looping. If the sound is already playing, it will be played again, concurrently. You need to stop the
     * sound via a call to {@link #stop(long)} using the returned id.
     *
     * @param volume the volume in the range [0, 1]
     * @return the id of the sound instance if successful, or -1 on failure.
     */
    long loop(float volume);

    /**
     * Plays the sound, looping. If the sound is already playing, it will be played again, concurrently. You need to stop the
     * sound via a call to {@link #stop(long)} using the returned id. Note that (with the exception of the web backend) panning
     * only works for mono sounds, not for stereo sounds!
     *
     * @param volume the volume in the range [0,1]
     * @param pitch  the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
     * @param pan    panning in the range -1 (full left) to 1 (full right). 0 is center position.
     * @return the id of the sound instance if successful, or -1 on failure.
     */
    long loop(float volume, float pitch, float pan);

    /**
     * Stops playing all instances of this sound.
     */
    void stop();

    /**
     * Pauses all instances of this sound.
     */
    void pause();

    /**
     * Resumes all paused instances of this sound.
     */
    void resume();

    /**
     * Releases all the resources.
     */
    void dispose();

    /**
     * Stops the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}. If the sound is no
     * longer playing, this has no effect.
     *
     * @param soundId the sound id
     */
    void stop(long soundId);

    /**
     * Pauses the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}. If the sound is no
     * longer playing, this has no effect.
     *
     * @param soundId the sound id
     */
    void pause(long soundId);

    /**
     * Resumes the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}. If the sound is not
     * paused, this has no effect.
     *
     * @param soundId the sound id
     */
    void resume(long soundId);

    /**
     * Sets the sound instance with the given id to be looping. If the sound is no longer playing this has no effect.s
     *
     * @param soundId the sound id
     * @param looping whether to loop or not.
     */
    void setLooping(long soundId, boolean looping);

    /**
     * Changes the pitch multiplier of the sound instance with the given id as returned by {@link #play()} or
     * {@link #play(float)}. If the sound is no longer playing, this has no effect.
     *
     * @param soundId the sound id
     * @param pitch   the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
     */
    void setPitch(long soundId, float pitch);

    /**
     * Changes the volume of the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}. If the
     * sound is no longer playing, this has no effect.
     *
     * @param soundId the sound id
     * @param volume  the volume in the range 0 (silent) to 1 (max volume).
     */
    void setVolume(long soundId, float volume);

    /**
     * Sets the panning and volume of the sound instance with the given id as returned by {@link #play()} or {@link #play(float)}.
     * If the sound is no longer playing, this has no effect. Note that panning only works for mono sounds, not for stereo sounds!
     *
     * @param soundId the sound id
     * @param pan     panning in the range -1 (full left) to 1 (full right). 0 is center position.
     * @param volume  the volume in the range [0,1].
     */
    void setPan(long soundId, float pan, float volume);
}
