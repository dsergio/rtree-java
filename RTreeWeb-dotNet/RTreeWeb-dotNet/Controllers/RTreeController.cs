using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace RTreeWeb_dotNet.Controllers
{
    public class RTreeController : Controller
    {
        [Route("RTree")]
        [Route("RTree/Index")]
        [Route("RTree/{id?}")]
        public IActionResult Index(int? id)
        {
            return View();
        }
    }
}