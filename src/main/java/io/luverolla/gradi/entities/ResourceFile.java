package io.luverolla.gradi.entities;

import javax.persistence.*;

import io.luverolla.gradi.structures.CodedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gradi_resource_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceFile extends CodedEntity
{
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_code", nullable = false)
    private Resource resource;
}
