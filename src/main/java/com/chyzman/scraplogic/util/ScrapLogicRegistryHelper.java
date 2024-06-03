package com.chyzman.scraplogic.util;

import com.chyzman.scraplogic.ScrapLogic;
import net.minecraft.util.Identifier;

public class ScrapLogicRegistryHelper {
    public static Identifier id(String path) {
        return new Identifier(ScrapLogic.MODID, path);
    }

}
