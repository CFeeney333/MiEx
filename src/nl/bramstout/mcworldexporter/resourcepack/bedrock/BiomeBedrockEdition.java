/*
 * BSD 3-Clause License
 * 
 * Copyright (c) 2024, Bram Stout Productions
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package nl.bramstout.mcworldexporter.resourcepack.bedrock;

import java.awt.image.BufferedImage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nl.bramstout.mcworldexporter.Color;
import nl.bramstout.mcworldexporter.resourcepack.Biome;
import nl.bramstout.mcworldexporter.resourcepack.Tints;

public class BiomeBedrockEdition extends Biome{

	private JsonObject data;
	
	public BiomeBedrockEdition(String name, int id, JsonObject data) {
		super(name, id);
		this.data = data;
		calculateTints();
	}
	
	public BiomeBedrockEdition(BiomeBedrockEdition other, int newId) {
		super(other.getName(), newId);
		this.data = other.data;
		this.foliageColour = other.foliageColour;
		this.grassColour = other.grassColour;
		this.waterColour = other.waterColour;
	}
	
	@Override
	public void calculateTints() {
		int foliageColourI = 0xffffff;
		int grassColourI = 0xffffff;
		
		if(data != null) {
			foliageColourI = 0;
			grassColourI = 0;
			
			float downfall = 0.5f;
			float temperature = 0.5f;
			
			if(data.has("minecraft:climate")) {
				JsonObject climateObj = data.get("minecraft:climate").getAsJsonObject();
				JsonElement downfallObj = climateObj.get("downfall");
				if(downfallObj != null)
					downfall = downfallObj.getAsFloat();
				
				JsonElement temperatureObj = climateObj.get("temperature");
				if(temperatureObj != null)
					temperature = temperatureObj.getAsFloat();
				
				float tintX = Math.max(Math.min(temperature, 1.0f), 0.0f);
				float tintY = 1.0f - (Math.max(Math.min(downfall, 1.0f), 0.0f) * tintX);
				tintX = 1.0f - tintX;
				
				if(foliageColourI == 0) {
					BufferedImage foliageColorMap = Tints.getFoliageColorMap();
					if(foliageColorMap != null) {
						int tintXI = (int) (tintX * ((float) (foliageColorMap.getWidth()-1)));
						int tintYI = (int) (tintY * ((float) (foliageColorMap.getHeight()-1)));
						foliageColourI = foliageColorMap.getRGB(tintXI, tintYI);
					}
				}
				
				if(grassColourI == 0) {
					BufferedImage grassColorMap = Tints.getGrassColorMap();
					if(grassColorMap != null) {
						int tintXI = (int) (tintX * ((float) (grassColorMap.getWidth()-1)));
						int tintYI = (int) (tintY * ((float) (grassColorMap.getHeight()-1)));
						
						grassColourI = grassColorMap.getRGB(tintXI, tintYI);
					}
				}
				
				foliageColour = new Color(foliageColourI);
				grassColour = new Color(grassColourI);
			}
		}
	}
	
}
