<?php

namespace App\Repository;

use App\Entity\ProduitCommande;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<ProduitCommande>
 *
 * @method ProduitCommande|null find($id, $lockMode = null, $lockVersion = null)
 * @method ProduitCommande|null findOneBy(array $criteria, array $orderBy = null)
 * @method ProduitCommande[]    findAll()
 * @method ProduitCommande[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ProduitCommandeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, ProduitCommande::class);
    }

//    /**
//     * @return ProduitCommande[] Returns an array of ProduitCommande objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('p.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?ProduitCommande
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }


public function findProductsByOrderReference(string $orderReference): array
    {
        return $this->createQueryBuilder('pc')
            ->leftJoin('pc.commande', 'c')
            ->andWhere('c.refCommande = :orderReference')
            ->setParameter('orderReference', $orderReference)
            ->getQuery()
            ->getResult();
    }



}
