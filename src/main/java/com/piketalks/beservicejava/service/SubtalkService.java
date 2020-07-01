package com.piketalks.beservicejava.service;

import com.piketalks.beservicejava.dto.SubtalkDto;
import com.piketalks.beservicejava.exceptions.PiketalksException;
import com.piketalks.beservicejava.mapper.SubtalkMapper;
import com.piketalks.beservicejava.model.Subtalk;
import com.piketalks.beservicejava.model.User;
import com.piketalks.beservicejava.repository.SubtalkRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubtalkService {

    private SubtalkRepository subtalkRepository;
    private final SubtalkMapper subtalkMapper;
    private final AuthService authService;

    @Transactional
    public SubtalkDto save(SubtalkDto subtalkDto){
        User currentUser = authService.getCurrentUser();

        Subtalk subtalk = subtalkMapper.maptoToSubtalk(subtalkDto, authService.getCurrentUser());
        Subtalk save = subtalkRepository.save(subtalk);
        subtalkDto.setId(save.getId());
        return subtalkDto;
    }


    @Transactional(readOnly = true)
    public List<SubtalkDto> getAll(){
        return subtalkRepository.findAll().stream().map(subtalkMapper::mapSubtalkToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubtalkDto getSubtalk(Long id){
        Subtalk subtalk = subtalkRepository.findById(id)
                .orElseThrow(() -> new PiketalksException("No subtalk with id "+ id.toString()));

        return subtalkMapper.mapSubtalkToDto(subtalk);
    }
}
