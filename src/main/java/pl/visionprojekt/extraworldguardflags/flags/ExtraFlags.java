package pl.visionprojekt.extraworldguardflags.flags;

import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class ExtraFlags {
    private static final List<String> INBUILT_FLAGS_LIST = new ArrayList<>();
    public static final List<String> INBUILT_FLAGS = Collections.unmodifiableList(INBUILT_FLAGS_LIST);
    public static final SetFlag<BlockType> DENY_BLOCK_PHYSICS =
            register(new SetFlag<>("deny-block-physics",
                    new RegistryFlag<>(null, BlockType.REGISTRY))); //TODO: fix this, since it doesn't work properly;
    // it should stop lanterns etc... from breaking due to missing support block

    public static final SetFlag<BlockType> DENY_BLOCK_INTERACT =
            register(new SetFlag<>("deny-block-interact",
                    new RegistryFlag<>(null, BlockType.REGISTRY)));
    public static final SetFlag<BlockType> DENY_FALLING_BLOCK =
            register(new SetFlag<>("deny-block-falling",
                    new RegistryFlag<>(null, BlockType.REGISTRY)));
    public static final SetFlag<String> ENTRY_COMMANDS =
            register(new SetFlag<>("entry-commands", new CommandStringFlag(null)));
    public static final SetFlag<String> EXIT_COMMANDS =
            register(new SetFlag<>("exit-commands", new CommandStringFlag(null)));
    public static final StateFlag HOPPER_ITEM_PICKUP = register(new StateFlag("hopper-item-pickup", true));

    public static final StateFlag FLIGHT = register(new StateFlag("flight", true));

    private ExtraFlags() {

    }

    private static <T extends Flag<?>> T register(final T flag) throws FlagConflictException {
        WorldGuard.getInstance().getFlagRegistry().register(flag);
        INBUILT_FLAGS_LIST.add(flag.getName());
        return flag;
    }

    private static <T extends Flag<?>> T register(final T flag, Consumer<T> cfg) throws FlagConflictException {
        T f = register(flag);
        cfg.accept(f);
        return f;
    }

    /**
     * Dummy method to call that initialises the class.
     */
    public static void registerAll() {
    }
}
