// src/main/java/com/rpg/composite/GroupComponent.java
package com.rpg.composite;

import com.rpg.core.Character;
import java.util.List;

public interface GroupComponent {
    String getName();
    int getPowerLevel();
    List<Character> flatten();

    // Uniformisation: une description lisible partout
    default String getDescription() { return getName() + " (power=" + getPowerLevel() + ")"; }

    default void add(GroupComponent child) { /* nop */ }
    default void remove(GroupComponent child) { /* nop */ }
}
