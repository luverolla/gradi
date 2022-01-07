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
	@GeneratedValue(generator = "gradi_resource_file_sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "gradi_resource_file_sequence", sequenceName = "gradi_resource_file_sequence")
	private Long index;

	@Column(nullable = false, unique = true)
	private String uri;
	
	@Column(nullable = false, unique = true)
	private String path;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "resource_code", nullable = false)
    private Resource resource;
}
