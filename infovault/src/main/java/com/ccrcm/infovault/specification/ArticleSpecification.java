package com.ccrcm.infovault.specification;

import com.ccrcm.infovault.entity.*;
import com.ccrcm.infovault.enums.ArticleStatus;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ArticleSpecification {

    public static Specification<Article> filterArticles(
            List<Long> countryIds,
            List<Long> updateTypeIds,
            List<Long> clinicalTypeIds,
            String search
    ) {

        return (root, query, cb) -> {

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Active
            predicates.add(cb.isTrue(root.get("active")));

            // Published
            predicates.add(
                    cb.equal(root.get("status"), ArticleStatus.PUBLISHED)
            );

            // Country filter
            if (countryIds != null && !countryIds.isEmpty()) {
                predicates.add(root.get("country").get("id").in(countryIds));
            }

            // Update type filter
            if (updateTypeIds != null && !updateTypeIds.isEmpty()) {
                predicates.add(root.get("updateType").get("id").in(updateTypeIds));
            }

            // Clinical type filter
            if (clinicalTypeIds != null && !clinicalTypeIds.isEmpty()) {
                predicates.add(root.get("clinicalType").get("id").in(clinicalTypeIds));
            }

            // Search
            if (search != null && !search.trim().isEmpty()) {

                String pattern = "%" + search.trim().toLowerCase() + "%";

                Join<Article, Source> sourceJoin = root.join("source", JoinType.LEFT);
                Join<Article, Country> countryJoin = root.join("country", JoinType.LEFT);
                Join<Article, UpdateType> updateTypeJoin = root.join("updateType", JoinType.LEFT);
                Join<Article, ClinicalType> clinicalTypeJoin = root.join("clinicalType", JoinType.LEFT);
                Join<Article, ImpactLevel> impactJoin = root.join("impactLevel", JoinType.LEFT);

                List<Predicate> searchPredicates = new ArrayList<>();

                searchPredicates.add(cb.like(cb.lower(root.get("title")), pattern));
                searchPredicates.add(cb.like(cb.lower(root.get("articleContent")), pattern));
                searchPredicates.add(cb.like(cb.lower(sourceJoin.get("name")), pattern));
                searchPredicates.add(cb.like(cb.lower(countryJoin.get("name")), pattern));
                searchPredicates.add(cb.like(cb.lower(updateTypeJoin.get("name")), pattern));
                searchPredicates.add(cb.like(cb.lower(clinicalTypeJoin.get("name")), pattern));
                searchPredicates.add(cb.like(cb.lower(impactJoin.get("name")), pattern));

                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));

                /*
                 * Relevance ranking
                 * Title match -> highest
                 * Article content -> medium
                 * Others -> low
                 */

                Expression<Integer> relevanceScore = cb.<Integer>selectCase()
                        .when(cb.like(cb.lower(root.get("title")), pattern), 1)
                        .otherwise(2);

                query.orderBy(cb.asc(relevanceScore));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
