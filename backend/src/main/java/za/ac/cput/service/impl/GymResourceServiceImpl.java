package za.ac.cput.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.GymResource;
import za.ac.cput.repository.IGymResourceRepository;
import za.ac.cput.service.IGymResourceService;
import za.ac.cput.util.Helper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class GymResourceServiceImpl implements IGymResourceService {

    private static final int MAX_TITLE = 255;
    private static final int MAX_SUMMARY = 500;
    private static final int MAX_BODY = 4000;
    private static final int MAX_TAGS = 10;
    private static final int MAX_TAG_LENGTH = 40;

    private final IGymResourceRepository repository;

    @Autowired
    public GymResourceServiceImpl(IGymResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public GymResource create(GymResource resource) {
        if (resource == null) {
            return null;
        }
        GymResource cleaned = validateAndClean(resource);

        String id = cleaned.getResourceId();
        if (Helper.isNullOrEmpty(id)) {
            cleaned = new GymResource.Builder().copy(cleaned).setResourceId(Helper.generateId()).build();
        } else if (repository.existsById(id)) {
            throw new IllegalArgumentException("A resource with this ID already exists.");
        }
        return repository.save(cleaned);
    }

    @Override
    public GymResource read(String resourceId) {
        if (resourceId == null) {
            return null;
        }
        return repository.findById(resourceId).orElse(null);
    }

    @Override
    public GymResource update(GymResource resource) {
        if (resource == null || Helper.isNullOrEmpty(resource.getResourceId())) {
            return null;
        }
        if (!repository.existsById(resource.getResourceId())) {
            return null;
        }
        return repository.save(validateAndClean(resource));
    }

    @Override
    public boolean delete(String resourceId) {
        if (resourceId == null || !repository.existsById(resourceId)) {
            return false;
        }
        repository.deleteById(resourceId);
        return true;
    }

    @Override
    public List<GymResource> getAll() {
        List<GymResource> all = new ArrayList<>(repository.findAll());
        all.sort(Comparator
                .comparing((GymResource r) -> r.getCategory() == null ? 99 : r.getCategory().ordinal())
                .thenComparing(r -> r.getTitle() == null ? "" : r.getTitle().toLowerCase()));
        return all;
    }

    private GymResource validateAndClean(GymResource resource) {
        if (resource.getCategory() == null) {
            throw new IllegalArgumentException("Category must be manual, nutrition or tip.");
        }
        String title = trim(resource.getTitle());
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        if (title.length() > MAX_TITLE) {
            throw new IllegalArgumentException("Title must be at most " + MAX_TITLE + " characters.");
        }
        String summary = trim(resource.getSummary());
        if (summary.length() > MAX_SUMMARY) {
            throw new IllegalArgumentException("Summary must be at most " + MAX_SUMMARY + " characters.");
        }
        String body = trim(resource.getBody());
        if (body.isEmpty()) {
            throw new IllegalArgumentException("Details are required.");
        }
        if (body.length() > MAX_BODY) {
            throw new IllegalArgumentException("Details must be at most " + MAX_BODY + " characters.");
        }

        List<String> tags = new ArrayList<>();
        if (resource.getTags() != null) {
            for (String raw : resource.getTags()) {
                String tag = trim(raw);
                if (tag.isEmpty() || tags.contains(tag)) {
                    continue;
                }
                if (tag.length() > MAX_TAG_LENGTH) {
                    throw new IllegalArgumentException("Each tag must be at most " + MAX_TAG_LENGTH + " characters.");
                }
                tags.add(tag);
            }
        }
        if (tags.size() > MAX_TAGS) {
            throw new IllegalArgumentException("Use at most " + MAX_TAGS + " tags.");
        }

        return new GymResource.Builder()
                .setResourceId(resource.getResourceId())
                .setCategory(resource.getCategory())
                .setTitle(title)
                .setSummary(summary.isEmpty() ? null : summary)
                .setBody(body)
                .setTags(tags)
                .build();
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
