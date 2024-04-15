<?php

namespace App\Entity;

use App\Repository\ReservationsRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ReservationsRepository::class)]
class Reservations
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255, nullable: true)]
    #[Assert\NotBlank(message: "Date cannot be blank")]
    private ?string $date = null;

    #[ORM\Column(length: 255, nullable: true)]
    #[Assert\NotBlank(message: "Duree cannot be blank")]
    private ?string $duree = null;

    #[ORM\Column(nullable: true)]
    #[Assert\NotBlank(message: "Prix cannot be blank")]
    private ?int $prix = null;

    /**
     * @var Collection<int, Terrain>
     */
    #[ORM\ManyToMany(targetEntity: Terrain::class, inversedBy: 'reservations')]
    private Collection $Reservations;

    #[ORM\ManyToOne(inversedBy: 'reservations')]
    private ?User $User = null;

    /**
     * @var Collection<int, Equipements>
     */
    #[ORM\ManyToMany(targetEntity: Equipements::class, inversedBy: 'reservations')]
    private Collection $Equipements;

    public function __construct()
    {
        $this->Reservations = new ArrayCollection();
        $this->equipements = new ArrayCollection();
        $this->Equipements = new ArrayCollection();
    }

    // Getters and setters...

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDate(): ?string
    {
        return $this->date;
    }

    public function setDate(?string $date): static
    {
        $this->date = $date;

        return $this;
    }

    public function getDuree(): ?string
    {
        return $this->duree;
    }

    public function setDuree(?string $duree): static
    {
        $this->duree = $duree;

        return $this;
    }

    public function getPrix(): ?int
    {
        return $this->prix;
    }

    public function setPrix(?int $prix): static
    {
        $this->prix = $prix;

        return $this;
    }

    /**
     * @return Collection<int, Terrain>
     */
    public function getReservations(): Collection
    {
        return $this->Reservations;
    }

    public function setReservations(?Reservations $reservations): self
    {
        $this->reservations = $reservations;

        return $this;
    }

    public function addReservation(Terrain $reservation): static
    {
        if (!$this->Reservations->contains($reservation)) {
            $this->Reservations->add($reservation);
        }

        return $this;
    }

    public function removeReservation(Terrain $reservation): static
    {
        $this->Reservations->removeElement($reservation);

        return $this;
    }

    public function getUser(): ?User
    {
        return $this->User;
    }

    public function setUser(?User $User): static
    {
        $this->User = $User;

        return $this;
    }

    /**
     * @return Collection<int, Equipements>
     */
    public function getEquipements(): Collection
    {
        return $this->Equipements;
    }

    public function addEquipement(Equipements $equipement): static
    {
        if (!$this->Equipements->contains($equipement)) {
            $this->Equipements->add($equipement);
        }

        return $this;
    }

    public function removeEquipement(Equipements $equipement): static
    {
        $this->Equipements->removeElement($equipement);

        return $this;
    }
}
