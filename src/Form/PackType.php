<?php

namespace App\Form;

use App\Entity\Pack;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
class PackType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
{
    $builder
        ->add('nom')
        ->add('description')
        ->add('reduction')
        ->add('image', FileType::class, [
            'label' => 'Charger une image',
            'required' => false,
            'mapped' => false,
            'attr' => ['class' => 'form-control'],
        ])
        ->add('duree', ChoiceType::class, [ // Add 'typesport' field
            'label' => 'Type',
            'choices' => [
                '1 mois' => '1 mois',
                '2 mois' => '2 mois',
                '3 mois' => '3 mois',
                '4 mois' => '4 mois',
                '5 mois' => '5 mois',
                '6 mois' => '6 mois',
                '7 mois' => '7 mois',
                '8 mois' => '8 mois',
                '9 mois' => '9 mois',
                '10 mois' => '10 mois',
                '11 mois' => '11 mois',
                '12 mois' => '12 mois',
            ],
            'placeholder' => 'Choisir la dureé' // Placeholder ajouté ici
        ]); // Ajout de la parenthèse fermante ici
}


    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Pack::class,
            'ancienne_image' => null, // Par défaut, aucune ancienne image
        ]);
    }
}
