package com.realworld.conduit.application.controller;

import com.realworld.conduit.application.resource.article.SingleArticleResponse;
import com.realworld.conduit.domain.exception.ResourceNotFoundException;
import com.realworld.conduit.domain.object.ArticleWithSummary;
import com.realworld.conduit.domain.object.User;
import com.realworld.conduit.domain.service.ArticleFavoriteService;
import com.realworld.conduit.domain.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/articles/{slug}/favorite")
public class ArticleFavoriteController {
  private final ArticleService articleService;
  private final ArticleFavoriteService articleFavoriteService;
  @PostMapping
  public ResponseEntity favoriteArticle(
    @PathVariable("slug") String slug,
    @AuthenticationPrincipal User user
  ) {
    ArticleWithSummary article = articleService.findBySlug(slug, user).orElseThrow(
      ResourceNotFoundException::new);
    articleFavoriteService.create(article, user);
    final int favoriteCount = articleFavoriteService.countByAuthorId(article.getId());
    final boolean isFavorited = articleFavoriteService.
      find(article.getId(), user.getId()).isPresent();
    return ResponseEntity.ok(SingleArticleResponse.from(article, isFavorited, favoriteCount));
  }

  @DeleteMapping
  public ResponseEntity unfavoriteArticle(
    @PathVariable("slug") String slug,
    @AuthenticationPrincipal User user
  ) {
    ArticleWithSummary article = articleService.findBySlug(slug, user).orElseThrow(
      ResourceNotFoundException::new);
    articleFavoriteService
      .find(article.getId(), user.getId())
      .ifPresent(
        articleFavoriteService::remove
      );
    final int favoriteCount = articleFavoriteService.countByAuthorId(article.getId());
    final boolean isFavorited = articleFavoriteService.
      find(article.getId(), user.getId()).isPresent();
    return ResponseEntity.ok(SingleArticleResponse.from(article, isFavorited, favoriteCount));
  }
}
