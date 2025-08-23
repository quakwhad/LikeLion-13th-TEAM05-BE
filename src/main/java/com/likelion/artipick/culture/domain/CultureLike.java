package com.likelion.artipick.culture.domain;

import com.likelion.artipick.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "culture_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "culture_id"})
)
public class CultureLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culture_id", nullable = false)
    private Culture culture;

    private boolean liked;

    public CultureLike(User user, Culture culture) {
        this.user = user;
        this.culture = culture;
        this.liked = true;
    }

    public void toggle() {
        this.liked = !this.liked;
    }
}
