package com.navya.interview_coach.controller;

import com.navya.interview_coach.entity.WeakArea;
import com.navya.interview_coach.repositary.WeakAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weak-areas")
public class WeakAreaController {

    @Autowired
    private WeakAreaRepository weakAreaRepository;

    @GetMapping
    public List<WeakArea> getAllWeakAreas() {
        return weakAreaRepository.findAll();
    }
}
