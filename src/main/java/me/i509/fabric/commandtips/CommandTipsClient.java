package me.i509.fabric.commandtips;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.i509.fabric.commandtips.api.config.CommandTipsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class CommandTipsClient implements ClientModInitializer {
	public static final FabricKeyBinding CACHE_ENTITY = FabricKeyBinding.Builder.create(CommandTipsClient.id("cache_entity"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "commandtips").build();
	private static final String MODID = "commandtips";
	private boolean previousKeyState = false;
	private CommandTipsConfig config = new CommandTipsConfig();

	private static Identifier id(String path) {
		return new Identifier(CommandTipsClient.MODID, path);
	}

	private UUID cachedUUID;
	private EntityType<?> cachedEntityType;

	public CommandTipsClient() {
		this.instance = this;
	}

	private static final Logger LOGGER = LogManager.getLogger(CommandTipsClient.class);
	private static CommandTipsClient instance;

	private boolean enabled = false;
	private CommandTipsNetworking networking = new CommandTipsNetworking();
	private SuggestionDispatcher suggestionDispatcher = new SuggestionDispatcher();

	public static CommandTipsClient getInstance() {
		return CommandTipsClient.instance;
	}

	public CommandTipsNetworking getNetworking() {
		return this.networking;
	}

	public SuggestionDispatcher getSuggestionDispatcher() {
		return this.suggestionDispatcher;
	}

	public Optional<UUID> getCachedEntityUUID() {
		return Optional.ofNullable(this.cachedUUID);
	}

	public Optional<EntityType<?>> getCachedEntityType() {
		return Optional.ofNullable(this.cachedEntityType);
	}

	public void setCachedEntity(UUID uuid, EntityType<?> entityType) {
		this.cachedUUID = checkNotNull(uuid, "UUID being cached was null");
		this.cachedEntityType = checkNotNull(entityType, "EntityType being cached was null.");
	}

	private void onClientTick(MinecraftClient client) {
		if (CommandTipsClient.CACHE_ENTITY.isPressed() && previousKeyState != CommandTipsClient.CACHE_ENTITY.isPressed()) {
			previousKeyState = true;
			if (client.getNetworkHandler() != null) {
				Entity entity = client.targetedEntity;

				if (entity != null && client.player != null) {
					UUID entityUUID = entity.getUuid();
					EntityType<?> entityType = entity.getType();
					this.setCachedEntity(entityUUID, entityType);
					client.player.addMessage(
						new TranslatableText("commandtips.cmd.cached", new TranslatableText(entityType.getTranslationKey()), entityUUID.toString()).formatted(Formatting.GRAY), false
					);
				}
			}
		} else if (!CommandTipsClient.CACHE_ENTITY.isPressed() && previousKeyState) {
			previousKeyState = false;
		}
	}

	@Override
	public void onInitializeClient() {
		if (this.enabled) {
			return;
		}

		this.enabled = true;

		KeyBindingRegistry.INSTANCE.register(CommandTipsClient.CACHE_ENTITY);
		ClientTickCallback.EVENT.register(this::onClientTick);
	}

	public CommandTipsConfig getConfig() {
		return config;
	}
}
