package com.dam1rka.musicserver.controllers.api.channel;

import com.dam1rka.musicserver.dtos.channel.ClientChannelPayload;
import com.dam1rka.musicserver.dtos.channel.CreateChannelDto;
import com.dam1rka.musicserver.dtos.channel.ServerChannelPayload;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.entities.channel.ChannelActionEnum;
import com.dam1rka.musicserver.services.UserService;
//import com.dam1rka.musicserver.services.channel.ChannelEventService;
import com.dam1rka.musicserver.services.channel.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final UserService userService;
    private final ChannelService channelService;
    private final SimpMessagingTemplate messagingService;

    @GetMapping("/list")
    public ResponseEntity<?> getChannels(Principal principal) {
        UserEntity user = userService.checkUser(principal);

        if(Objects.nonNull(user)) {
            return ResponseEntity.ok(channelService.getChannels());
        }

        return ResponseEntity.badRequest().body("You're not authorized");
    }

    @GetMapping("/get/{channelId}")
    public ResponseEntity<?> getChannel(@PathVariable() Long channelId, Principal principal) {
        UserEntity user = userService.checkUser(principal);

        if(Objects.nonNull(user)) {
            return ResponseEntity.ok(channelService.getChannel(channelId));
        }

        return ResponseEntity.badRequest().body("You're not authorized");
    }

    @GetMapping("/{channelId}/chat")
    public ResponseEntity<?> getChannelChat(@PathVariable() Long channelId, Principal principal) {
        UserEntity user = userService.checkUser(principal);

        if(Objects.nonNull(user)) {
            return ResponseEntity.ok(channelService.getChat(channelId));
        }

        return ResponseEntity.badRequest().body("You're not authorized");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createChannel(CreateChannelDto createChannelDto, Principal principal) {
        UserEntity user = userService.checkUser(principal);

        if(Objects.nonNull(user)) {
            channelService.createChannel(user, createChannelDto);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("You're not authorized");
    }

    @MessageMapping("/events")
    public void processMessage(@Payload ClientChannelPayload payload, BearerTokenAuthenticationToken token) {
        messagingService.setUserDestinationPrefix("/channel");
        try {
            UserEntity user = userService.getUserByToken(token.getToken());

            switch (payload.getAction()) {

                case CONNECT -> channelService.userActionToChannel(payload.getChannelId(), user, ChannelActionEnum.CONNECTED);
                case DISCONNECT -> channelService.userActionToChannel(payload.getChannelId(), user, ChannelActionEnum.DISCONNECTED);
                case MESSAGE -> {
                    channelService.saveMessage(payload.getChannelId(), user, payload.getContent());

                    ServerChannelPayload serverPayload = new ServerChannelPayload();
                    serverPayload.setChannelId(payload.getChannelId());
                    serverPayload.setCreated(payload.getCreated());
                    serverPayload.setAction(payload.getAction());
                    serverPayload.setContent(payload.getContent());
                    serverPayload.setFrom(user.getUsername());

                    messagingService.convertAndSendToUser(
                            payload.getChannelId().toString(),"/queue/events",
                            serverPayload);
                }
                case PLAY -> {
                }
                case PAUSE -> {
                }
                case PLAYNEXT -> {
                }
                case PLAYPREV -> {
                }
            }

        } catch (UserPrincipalNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
