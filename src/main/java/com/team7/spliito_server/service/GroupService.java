package com.team7.spliito_server.service;

import com.team7.spliito_server.dto.CreateGroupRequest;
import com.team7.spliito_server.dto.GroupResponse;
import com.team7.spliito_server.model.Group;
import com.team7.spliito_server.model.User;
import com.team7.spliito_server.repository.GroupRepository;
import com.team7.spliito_server.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    /**
     * 그룹 생성 또는 업데이트
     *
     * @param request CreateGroupRequest 요청 DTO
     * @return 생성된 그룹의 URL (group/{id})
     */
    @Transactional
    public String createOrUpdateGroup(CreateGroupRequest request) {
        // 기존 그룹이 존재하면 가져오고, 없으면 새로 생성
        Group group = groupRepository.findByName(request.getGroupName())
                .orElseGet(() -> new Group(request.getGroupName()));

        // 기존 멤버 이름 목록을 수집하여, 중복 추가를 방지
        List<String> existingMemberNames = group.getMembers() != null ?
                group.getMembers().stream()
                        .map(User::getName)
                        .collect(Collectors.toList())
                : List.of(); // 새 그룹이면 빈 리스트로 초기화

        // 요청된 멤버 이름 중 기존에 없는 멤버만 추가
        List<User> newMembers = request.getMemberNames().stream()
                .filter(name -> !existingMemberNames.contains(name)) // 기존 멤버와 중복되지 않는 이름만 추가
                .map(name -> new User(name, group))
                .collect(Collectors.toList());

        // 그룹에 멤버 추가
        group.getMembers().addAll(newMembers);

        // 그룹 및 멤버를 데이터베이스에 저장
        Group savedGroup = groupRepository.save(group);

        // 생성된 그룹의 URL 반환
        return "group/" + savedGroup.getId();
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> getAllGroups() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return groupRepository.findAll().stream()
                .map(group -> new GroupResponse(
                        group.getId(),
                        group.getName(),
                        group.getCreatedDate().format(formatter),
                        group.getMembers().stream()
                                .map(member -> member.getName())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
