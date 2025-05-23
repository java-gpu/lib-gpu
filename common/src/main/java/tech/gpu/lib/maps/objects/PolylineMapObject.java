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

package tech.gpu.lib.maps.objects;

import tech.gpu.lib.maps.MapObject;
import tech.gpu.lib.math.Polyline;

/** @brief Represents {@link Polyline} map objects */
public class PolylineMapObject extends MapObject {

	private Polyline polyline;

	/** @return polyline shape */
	public Polyline getPolyline () {
		return polyline;
	}

	/** @param polyline new object's polyline shape */
	public void setPolyline (Polyline polyline) {
		this.polyline = polyline;
	}

	/** Creates empty polyline */
	public PolylineMapObject () {
		this(new float[0]);
	}

	/** @param vertices polyline defining vertices */
	public PolylineMapObject (float[] vertices) {
		polyline = new Polyline(vertices);
	}

	/** @param polyline the polyline */
	public PolylineMapObject (Polyline polyline) {
		this.polyline = polyline;
	}

}
