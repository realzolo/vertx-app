package com.onezol.vertx.framework.component.dictionary.controller;

import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.component.dictionary.model.Dictionary;
import com.onezol.vertx.framework.component.dictionary.model.DictionaryEntity;
import com.onezol.vertx.framework.component.dictionary.service.DictionaryHelper;
import com.onezol.vertx.framework.component.dictionary.service.DictionaryService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "数据字典")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @Operation(summary = "获取字典", description = "根据字典分组获取字典项列表")
    @GetMapping
    @PreAuthorize("@Security.hasPermission('system:dictionary:list')")
    public GenericResponse<List<DictionaryEntry>> getDictItemsByGroup(@RequestParam("group") String group) {
        List<DictionaryEntry> dictionary = DictionaryHelper.get(group);
        return ResponseHelper.buildSuccessfulResponse(dictionary);
    }

    @Operation(summary = "获取字典详情", description = "根据字典ID获取字典详情")
    @GetMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:dictionary:detail')")
    public GenericResponse<Dictionary> getDictionaryById(@PathVariable("id") Long id) {
        DictionaryEntity dictionary = dictionaryService.getById(id);
        Dictionary bean = BeanUtils.toBean(dictionary, Dictionary.class);
        return ResponseHelper.buildSuccessfulResponse(bean);
    }

    @Operation(summary = "新增字典", description = "新增字典分组/字典项")
    @PostMapping
    @PreAuthorize("@Security.hasPermission('system:dictionary:create')")
    public GenericResponse<Void> createDictionary(@RequestBody Dictionary dictionary) {
        dictionaryService.createDictionary(dictionary);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "更新字典")
    @PutMapping
    @PreAuthorize("@Security.hasPermission('system:dictionary:update')")
    public GenericResponse<Void> updateDictionary(@RequestBody Dictionary dictionary) {
        DictionaryEntity entity = BeanUtils.toBean(dictionary, DictionaryEntity.class);
        dictionaryService.updateById(entity);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "删除字典")
    @DeleteMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:dictionary:delete')")
    public GenericResponse<Void> deleteDictionary(@PathVariable("id") Long id) {
        boolean ok = dictionaryService.removeById(id);
        if (!ok) {
            return ResponseHelper.buildFailedResponse("删除字典失败");
        }
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "获取字典分组列表")
    @GetMapping("/groups")
    @PreAuthorize("@Security.hasPermission('system:dictionary:list')")
    public GenericResponse<List<Dictionary>> getDictionaryGroups() {
        List<Dictionary> groups = dictionaryService.listGroups();
        return ResponseHelper.buildSuccessfulResponse(groups);
    }

    @Operation(summary = "获取字典分组下的字典项")
    @GetMapping("/items")
    @PreAuthorize("@Security.hasPermission('system:dictionary:list')")
    public GenericResponse<List<Dictionary>> getDictionaries(@RequestParam("groupId") Long groupId) {
        List<Dictionary> groups = dictionaryService.listItems(groupId);
        return ResponseHelper.buildSuccessfulResponse(groups);
    }

}
