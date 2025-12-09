package io.github.robak132.mcrgb_forge;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Storage class for block colour data.
 */
@Data
public class BlockColourStorage {
    String block;
    List<SpriteDetails> spriteDetails = new ArrayList<>();

    public void addSpriteDetails(SpriteDetails details) {
        this.spriteDetails.add(details);
    }
}
