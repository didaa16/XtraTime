<?php

namespace App\Form;

use App\Entity\Abonnement;
use App\Entity\Pack;
use App\Entity\Terrain;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\TextType;


class AbonnementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        
            $builder
            ->add('date', DateType::class, [
                'label' => 'Date',
                'widget' => 'single_text',
                'required' => true,
            ])
            ->add('prix', IntegerType::class, [
                'label' => 'Prix',
                'required' => true,
            ])
            ->add('pack', EntityType::class, [
                'class' => 'App\Entity\Pack',
                'choice_label' => 'nom', // Le champ de l'entité à afficher dans la liste déroulante
            ])
            // Ajoutez le champ pour le terrain
            ->add('terrain', EntityType::class, [
                'class' => 'App\Entity\Terrain',
                'choice_label' => 'nom', // Le champ de l'entité à afficher dans la liste déroulante
            ])
            ->add('nomuser', TextType::class, [
                'label' => 'Nom de l\'Utilisateur',
                'required' => true,
            ])
            ->add('numtel', IntegerType::class, [
                'label' => 'Numéro de Téléphone',
                'required' => true,
            ])
            ->add('prixtotal', IntegerType::class, [
                'label' => 'Prix Total',
                'required' => true,
            ]);
        
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Abonnement::class,
            'packs' => [], // Déclaration de l'option 'packs'
            'terrains' => [],
        ]);
    }
}
