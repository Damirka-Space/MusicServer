package com.dam1rka.musicserver.services.channel;

import com.dam1rka.musicserver.dtos.channel.ChannelListDto;
import com.dam1rka.musicserver.dtos.channel.ChannelMessageDto;
import com.dam1rka.musicserver.dtos.channel.CreateChannelDto;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.entities.channel.*;
import com.dam1rka.musicserver.repositories.channel.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelChatRepository channelChatRepository;
    private final ChannelQueueRepository channelQueueRepository;
    private final ChannelUserRepository channelUserRepository;
    private final ChannelMessageRepository channelMessageRepository;

    @Transactional
    public void createChannel(UserEntity user, CreateChannelDto createChannelDto) {
        ChannelEntity channel = new ChannelEntity();
        channel.setTitle(createChannelDto.getTitle());
        channel.setDescription(createChannelDto.getDescription());
        channel.setOwner(user);
        channel.setCreated(new Date());
        channel.setChat(channelChatRepository.save(new ChannelChat()));
        channel.setQueue(channelQueueRepository.save((new ChannelQueue())));

        channelRepository.save(channel);
    }

    public ChannelListDto getChannel(Long channelId) {
        ChannelEntity channel = channelRepository.findById(channelId).orElse(null);

        if(Objects.nonNull(channel)) {
            return convertToDto(channel);
        }

        return null;
    }

    private ChannelListDto convertToDto(ChannelEntity channel) {
        ChannelListDto dto = new ChannelListDto();

        dto.setId(channel.getId());
        dto.setTitle(channel.getTitle());
        dto.setDescription(channel.getDescription());
        dto.setOwnerUsername(channel.getOwner().getUsername());

        List<ChannelUserEntity> userConnections = channel.getUsers();

        Long count = userConnections.stream()
                .map(ChannelUserEntity::getAction)
                .filter(channelActionEnum -> channelActionEnum.equals(ChannelActionEnum.CONNECTED))
                .count() - userConnections.stream()
                .map(ChannelUserEntity::getAction)
                .filter(channelActionEnum -> channelActionEnum.equals(ChannelActionEnum.DISCONNECTED))
                .count();

        dto.setUserCount(count);
        return dto;
    }

    public List<ChannelListDto> getChannels() {
        List<ChannelEntity> channels = channelRepository.findAll();

        List<ChannelListDto> dtos = new LinkedList<>();

        channels.forEach((channel) -> dtos.add(convertToDto(channel)));

        return dtos;
    }

    public List<ChannelMessageDto> getChat(Long channelId) {
        ChannelEntity channel = channelRepository.findById(channelId).orElse(null);

        List<ChannelMessageDto> messages = new LinkedList<>();

        if(Objects.nonNull(channel)) {
            channel.getChat().getMessages()
                    .stream().sorted(Comparator.comparing(ChannelMessage::getCreated))
                    .forEach((message) -> {
                ChannelMessageDto msg = new ChannelMessageDto();
                msg.setMessage(message.getMessage());
                msg.setCreated(message.getCreated());
                msg.setSender(message.getSender().getUsername());
                messages.add(msg);
            });
        }

        return messages;
    }

    @Transactional
    public void userActionToChannel(Long channelId, UserEntity user, ChannelActionEnum action) {
        ChannelEntity channel = channelRepository.findById(channelId).orElse(null);

        if(Objects.nonNull(channel)) {
            List<ChannelUserEntity> users = channel.getUsers();

            ChannelUserEntity userEntity = new ChannelUserEntity();
            userEntity.setUser(user);
            userEntity.setCreated(new Date());
            userEntity.setAction(action);

            userEntity = channelUserRepository.save(userEntity);

            users.add(userEntity);

            channel.setUsers(users);

            channelRepository.save(channel);
        }
    }

    @Transactional
    public void saveMessage(Long channelId, UserEntity user, String message) {
        ChannelEntity channel = channelRepository.findById(channelId).orElse(null);

        if(Objects.nonNull(channel)) {
            List<ChannelMessage> messages = channel.getChat().getMessages();

            ChannelMessage msg = new ChannelMessage();
            msg.setMessage(message);
            msg.setSender(user);
            msg.setCreated(new Date());

            msg = channelMessageRepository.save(msg);

            messages.add(msg);
            channel.getChat().setMessages(messages);

            channelChatRepository.save(channel.getChat());
        }
    }

}
