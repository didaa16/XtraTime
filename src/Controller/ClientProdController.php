<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\ProduitRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\Persistence\ManagerRegistry;
use App\Entity\Produit;
use App\Form\ProduitType;
use Symfony\Component\Validator\Constraints\Valid;
use App\Entity\Ratingprod; // Ajout de l'importation manquante
use Symfony\Component\HttpFoundation\File\UploadedFile;
use App\Entity\Commande;
use App\Entity\ProduitCommande;
use App\Entity\Utilisateurs;
use App\Repository\ProduitCommandeRepository;


class ClientProdController extends AbstractController
{
    
    #[Route('/ClientProd', name: 'ClientProd')]
    public function ClientProd(Request $request, ProduitRepository $repository)
{
    // Récupération des données de formation depuis la base de données
    $produits = $repository->findAll(); // Fetches all products
    
    $sortBy = $request->query->get('sort_by', 'default'); // Récupérer l'option de tri ou utiliser une valeur par défaut

    


    switch ($sortBy) {
        
        case 'price_asc':
            $produits = $repository->findBy([], ['prix' => 'ASC']);
            break;
        case 'price_desc':
            $produits = $repository->findBy([], ['prix' => 'DESC']);
            break;
        case 'name_asc':
            $produits = $repository->findBy([], ['nom' => 'ASC']);
            break;
        default:
            $produits = $repository->findAll();
    }
    // Rendu de la vue avec les données de formation
    return $this->render('Client_prod/shop.html.twig', [
        'produits' => $produits, // Pass the products array
    ]);
}



    #[Route('/MoreDetailsShop/{ref}', name: 'MoreDetailsShop')]
    public function MoreDetailsShop($ref): Response
    {
        // Récupérer le produit depuis la base de données en fonction de l'identifiant
        $produit = $this->getDoctrine()->getRepository(Produit::class)->find($ref);

        if (!$produit) {
            throw $this->createNotFoundException('Produit non trouvé');
        }

        // Rendre la vue avec les détails du produit
        return $this->render('client_prod/single-shop.html.twig', [
            'produit' => $produit,
        ]);
    }
   



    #[Route('/AddCart/{ref}', name: 'AddCart')]
    public function addToCart(Request $request, string $ref): Response
    {
        // Récupérez l'utilisateur actuel (fixe pour l'exemple)
        $pseudo = 'dida';
    
        // Vérifiez si le produit existe dans la base de données
        $produit = $this->getDoctrine()->getRepository(Produit::class)->findOneBy(['ref' => $ref]);
    
        if (!$produit) {
            throw $this->createNotFoundException('Produit non trouvé');
        }
    
        // Récupérer l'utilisateur en utilisant son pseudo
        $utilisateur = $this->getDoctrine()->getRepository(Utilisateurs::class)->findOneBy(['pseudo' => $pseudo]);
    
        // Créer une nouvelle commande si elle n'existe pas encore pour l'utilisateur "bohmid"
        $commande = $this->getDoctrine()->getRepository(Commande::class)->findOneBy(['iduser' => $utilisateur, 'status' => 'enAttente']);
        if (!$commande) {
            $commande = new Commande();
            // Initialisez les autres champs de la commande si nécessaire
            $commande->setIduser($utilisateur);
            $commande->setStatus('enAttente');
            $commande->setPrix(0);
    
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($commande);
            $entityManager->flush();
        }
    
        // Ajouter le produit à la commande de l'utilisateur "bohmid"
        $produitCommande = new ProduitCommande();
        $produitCommande->setRef($produit->getRef());
        $produitCommande->setRefCommande($commande->getRefCommande());
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($produitCommande);
        $entityManager->flush();
    
        // Rediriger l'utilisateur vers la page shop après l'ajout au panier
        return $this->redirectToRoute('ClientProd'); 
    }
    
 

    #[Route('/myOrder', name: 'my_order')]
    public function myOrder(): Response
    {
        // Récupérer l'utilisateur actuel (fixe pour l'exemple)
        $pseudo = 'dida';
    
        // Récupérer l'utilisateur en utilisant son pseudo
        $utilisateur = $this->getDoctrine()->getRepository(Utilisateurs::class)->findOneBy(['pseudo' => $pseudo]);
    
        // Récupérer la commande de l'utilisateur
        $commande = $this->getDoctrine()->getRepository(Commande::class)->findOneBy(['iduser' => $utilisateur, 'status' => 'enAttente']);
    
        // Vérifier si la commande existe
        if ($commande) {
            // Récupérer les produits associés à cette commande
            $produitsCommande = $this->getDoctrine()->getRepository(ProduitCommande::class)->findBy(['refCommande' => $commande->getRefCommande()]);
    
            // Créer un tableau pour stocker le nombre de produits associés à chaque produit
            $produitsQuantite = [];
    
            // Compter le nombre de produits associés à chaque produit dans la commande
            foreach ($produitsCommande as $produitCommande) {
                $refProduit = $produitCommande->getRef();
                if (!isset($produitsQuantite[$refProduit])) {
                    $produitsQuantite[$refProduit] = 0;
                }
                $produitsQuantite[$refProduit]++;
            }
    
            // Récupérer les détails de chaque produit associé à la commande
            $produits = [];
            foreach ($produitsQuantite as $refProduit => $quantite) {
                $produit = $this->getDoctrine()->getRepository(Produit::class)->findOneBy(['ref' => $refProduit]);
                if ($produit) {
                    $produitDetails = [
                        'produit' => $produit,
                        'quantite' => $quantite
                    ];
                    $produits[] = $produitDetails;
                }


                # Récupérer la référence de la commande
                $refCommande = $produitCommande->getRefCommande();

                # Récupérer tous les produits associés à cette commande
                $produitsCommande = $this->getDoctrine()->getRepository(ProduitCommande::class)->findBy(['refCommande' => $refCommande]);

                # Initialiser la somme des prix des produits
                $sousTotal = 0;

               # Parcourir les produits associés à cette commande
               foreach ($produitsCommande as $produitCommande) {
               // Récupérer la référence du produit depuis ProduitCommande
               $refProduit = $produitCommande->getRef();
    
               // Récupérer le prix du produit en utilisant sa référence
               $produit = $this->getDoctrine()->getRepository(Produit::class)->findOneBy(['ref' => $refProduit]);
    
    // Vérifier si le produit existe
    if ($produit) {
        // Ajouter le prix du produit au sous-total
        $sousTotal += $produit->getPrix();
    }
}

# Mettre à jour l'attribut prix de la commande avec le sous-total calculé
$commande->setPrix($sousTotal);

# Enregistrer les modifications dans la base de données
$entityManager = $this->getDoctrine()->getManager();
$entityManager->persist($commande);
$entityManager->flush();





            }
    
            // Rendre la vue avec les détails de la commande
            return $this->render('Client_prod/cart.html.twig', [
                'commande' => $commande,
                'produits' => $produits
            ]);
        } else {
            // Si aucune commande n'est trouvée, vous pouvez renvoyer un message ou rediriger l'utilisateur
            // Dans cet exemple, nous redirigeons l'utilisateur vers une autre page
            return $this->redirectToRoute('ClientProd'); // Rediriger l'utilisateur vers la page d'accueil par exemple
        }
    }
    
    
   /*  -------------------supprimmer------------- */


   
   #[Route('/RemoveFromCart/{ref}', name: 'RemoveFromCart')]
   public function removeFromCart(Request $request, string $ref): Response
   {
       // Récupérez l'utilisateur actuel (fixe pour l'exemple)
       $pseudo = 'dida';
   
       // Récupérez le produit commandé à supprimer de la base de données
       $produitCommande = $this->getDoctrine()->getRepository(ProduitCommande::class)->findOneBy(['ref' => $ref]);
   
       if (!$produitCommande) {
           throw $this->createNotFoundException('Produit commandé non trouvé');
       }
   
       // Récupérez l'utilisateur en utilisant son pseudo
       $utilisateur = $this->getDoctrine()->getRepository(Utilisateurs::class)->findOneBy(['pseudo' => $pseudo]);
   
     /*   // Vérifier si le produit commandé appartient à l'utilisateur
       if ($produitCommande->getIduser() != $utilisateur->getId()) {
           throw $this->createAccessDeniedException('Vous n\'avez pas le droit de supprimer ce produit');
       } */
   
       // Récupérer la commande associée au produit commandé
       $commande = $this->getDoctrine()->getRepository(Commande::class)->findOneBy(['refCommande' => $produitCommande->getRefCommande()]);
   
       if (!$commande) {
           throw $this->createNotFoundException('Commande en cours non trouvée');
       }
   

       
       // Supprimer le produit de la commande
       $entityManager = $this->getDoctrine()->getManager();
       $entityManager->remove($produitCommande);
       $entityManager->flush();
   
       // Rediriger l'utilisateur vers la page de commande après la suppression
       return $this->redirectToRoute('my_order'); // Vous devez remplacer 'my_order' par le nom de votre route appropriée.
   }
   






    



}




