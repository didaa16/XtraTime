<?php

namespace App\Form;

use App\Entity\Equipements;
use App\Entity\Reservations;
use App\Entity\Terrain;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;


use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Type;
use Symfony\Component\Validator\Constraints\Regex;

class ReservationsType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('date', null, [
                'constraints' => [
                    new NotBlank(['message' => 'Le champ Date ne peut pas être vide.']),
                    new Regex([
                        'pattern' => '/^\d{2}\/\d{2}\/\d{4}$/',
                        'message' => 'Le champ Date doit être au format jj/mm/aaaa.',
                    ]),
                ],
            ])
            ->add('duree', null, [
                'constraints' => [
                    new NotBlank(['message' => 'Le champ Durée ne peut pas être vide.']),
                    new Type([
                        'type' => 'numeric',
                        'message' => 'Le champ Durée doit contenir uniquement des chiffres.',
                    ]),
                ],
            ])
            ->add('prix', null, [
                'constraints' => [
                    new NotBlank(['message' => 'Le champ Prix ne peut pas être vide.']),
                    new Type([
                        'type' => 'numeric',
                        'message' => 'Le champ Prix doit contenir uniquement des chiffres.',
                    ]),
                ],
            ])
            ->add('equipements', EntityType::class, [
                'class' => Equipements::class,
                'choice_label' => 'nom',
                'multiple' => true,
                'expanded' => true,
                'constraints' => [
                    new NotBlank(['message' => 'Le champ Equipements ne peut pas être vide.']),
                ],
            ])
            ->add('User', EntityType::class, [
                'class' => User::class,
                'choice_label' => 'id',
            ]);
    }

    

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Reservations::class,
            'equipements' => [], // Equipements choices will be passed from the controller
        ]);
    }

    // ...
}
